package com.springboot.blog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.security.JWTTokenProvider;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PostControllerITests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private PostRepository postRepository;

    // pass this token in request header
    private String token;
    // to write tests based on a saved entity
    private Post savedPost;

    // to use these dynamic values in tests
    private Long POST_ID;
    private Long CATEGORY_ID;

    // get a valid JWT token for testing
    private String generateTokenForTestUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")
                )
        );
        return jwtTokenProvider.generateToken(authentication);
    }


    @BeforeEach
    public void setup() {
        // delete existing posts and insert a new post
        postRepository.deleteAll();
        savedPost = postRepository.save(
                Post.builder()
                        .description("its a food review blog")
                        .title("Indian Cuisine")
                        .content("blah blah blah")
                        .category(
                                Category.builder()
                                        .name("food-blog")
                                        .description("food category")
                                        .build()
                        )
                        .build()
        );

        POST_ID = savedPost.getId();
        CATEGORY_ID = savedPost.getCategory().getId();

        token = generateTokenForTestUser();
    }

    @DisplayName("test to create a new post")
    @Test
    public void givenPostDtoResponseBody_whenCreatePost_thenReturnSavedPost()
            throws Exception {
        PostDto postDto = PostDto
                .builder()
                .title("post title")
                .content("post content")
                .description("some description")
                .categoryId(CATEGORY_ID)
                .build();

        ResultActions response = mockMvc.perform(post("/api/posts")
                .header("Authorization", "Bearer " + token)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",
                        CoreMatchers.is(postDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        CoreMatchers.is(postDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                        CoreMatchers.is(postDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.categoryId",
                        CoreMatchers.is(postDto.getCategoryId()), Long.class)
                );
    }

    @DisplayName("test to find a post by postId")
    @Test
    public void givenPostId_whenGetPostById_thenReturnPostDtoObject()
            throws Exception {

        ResultActions response = mockMvc.perform(get("/api/posts/{id}", POST_ID)
                .header("Authorization", "Bearer " + token)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",
                        CoreMatchers.is(savedPost.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        CoreMatchers.is(savedPost.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                        CoreMatchers.is(savedPost.getDescription())));
    }

    @DisplayName("test to delete a post by postId")
    @Test
    public void givenPostId_whenDeletePost_thenReturnPostIdDeleted()
            throws Exception {
        ResultActions response = mockMvc.perform(delete(
                "/api/posts/{postId}", POST_ID)
                .header("Authorization", "Bearer " + token)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(String.format("Post id = %d is deleted", POST_ID)));
    }

    @DisplayName("test to get a list of Posts by categoryId")
    @Test
    public void givenCategoryId_whenGetPostsByCategory_thenReturnListOfPostDto()
            throws Exception {
        ResultActions response = mockMvc.perform(get(
                "/api/posts/category/{categoryId}", CATEGORY_ID)
                .header("Authorization", "Bearer " + token)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryId",
                        CoreMatchers.is(CATEGORY_ID), Long.class));
    }
}
