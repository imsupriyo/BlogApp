package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.security.JWTTokenProvider;
import com.springboot.blog.service.PostService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PostController.class)
public class PostControllerTests {
    @MockBean
    private PostService postService;
    @MockBean
    private JWTTokenProvider jwtTokenProvider;
    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private PostDto postDto;


    @BeforeEach
    public void init() {
        postDto = PostDto
                .builder()
                .title("post title")
                .content("post content")
                .description("some description")
                .categoryId(1L)
                .build();

        when(jwtTokenProvider.validateToken(anyString()))
                .thenReturn(true);

        when(jwtTokenProvider.getUsername(anyString()))
                .thenReturn("admin");

        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User(
                        "admin",
                        "admin",
                        Set.of(new SimpleGrantedAuthority("ADMIN"))
                ));
    }

    @DisplayName("test to create a new post")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void givenPostDtoResponseBody_whenCreatePost_thenReturnSavedPost() throws Exception {
        long id = 1L;
        when(postService.createPost(any(PostDto.class)))
                .thenAnswer(invocation -> {
                    PostDto tempPostDto = invocation.getArgument(0);
                    tempPostDto.setId(id); // mock auto id generation
                    return tempPostDto;
                });

        // when
        ResultActions response = mockMvc.perform(post("/api/posts")
                .header("Authorization", "Bearer some.jwt.token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)));

        // then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id",
                        CoreMatchers.is(id),
                        Long.class
                ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",
                        CoreMatchers.is(postDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        CoreMatchers.is(postDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                        CoreMatchers.is(postDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.categoryId",
                        CoreMatchers.is(postDto.getCategoryId()),
                        Long.class)
                );
    }

    @DisplayName("test to find a post by postId")
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void givenPostId_whenGetPostById_thenReturnPostDtoObject() throws Exception {
        postDto.setId(1L);
        when(postService.getPostById(anyLong())).thenReturn(postDto);

        ResultActions response = mockMvc.perform(get("/api/posts/{id}", 1L)
                .header("Authorization", "Bearer some.jwt.token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id",
                        CoreMatchers.is(postDto.getId()),
                        Long.class
                ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",
                        CoreMatchers.is(postDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        CoreMatchers.is(postDto.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                        CoreMatchers.is(postDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.categoryId",
                        CoreMatchers.is(postDto.getCategoryId()),
                        Long.class)
                );
    }

    @DisplayName("test to update a post by postId")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void givenResponseBody_whenUpdatePost_thenReturnUpdatedPostDto() throws Exception {
        when(postService.updatePost(any(PostDto.class), any(Long.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PostDto responsePostDTo = PostDto.builder()
                .id(1L)
                .title("updated title")
                .description("updated description")
                .content("updated content")
                .categoryId(2L)
                .build();

        ResultActions response = mockMvc.perform(put("/api/posts/{postId}", 1L)
                .header("Authorization", "Bearer some.jwt.token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responsePostDTo)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.id",
                        CoreMatchers.is(responsePostDTo.getId()),
                        Long.class
                ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",
                        CoreMatchers.is(responsePostDTo.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content",
                        CoreMatchers.is(responsePostDTo.getContent())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                        CoreMatchers.is(responsePostDTo.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.categoryId",
                        CoreMatchers.is(responsePostDTo.getCategoryId()),
                        Long.class)
                );
    }

    @DisplayName("test to delete a post by postId")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void givenPostId_whenDeletePost_thenReturnPostIdDeleted() throws Exception {
        Long postId = 1L;
        ResultActions response = mockMvc.perform(delete("/api/posts/{postId}", postId)
                .header("Authorization", "Bearer some.jwt.token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.format("Post id = %d is deleted", postId)));
    }

    @DisplayName("test to get all posts by a categoryId")
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void givenCategoryId_whenGetPostsByCategory_thenReturnListOfPostDto() throws Exception {
        when(postService.getPostsByCategory(any(Long.class))).thenAnswer(invocation -> {
            Long cId = invocation.getArgument(0);
            List<PostDto> postDtoList = List.of(
                    PostDto.builder()
                            .title("title 1")
                            .description("description")
                            .content("content")
                            .categoryId(cId)
                            .build(),
                    PostDto.builder()
                            .title("title 2")
                            .description("description")
                            .content("content")
                            .categoryId(cId)
                            .build()
            );
            return postDtoList;
        });

        ResultActions response = mockMvc.perform(get("/api/posts/category/{categoryId}", 1L)
                .header("Authorization", "Bearer some.jwt.token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryId", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].categoryId", CoreMatchers.is(1)));
    }

    @DisplayName("test to get all the saved posts")
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void givenListOfPostDto_whenGetAllPosts_thenReturnListOfPostDto() throws Exception {
        when(postService.getAllPosts(anyInt(), anyInt(), anyString(), anyString())).thenAnswer(invocation ->
                PostResponse.builder()
                        .content(List.of(
                                        PostDto.builder()
                                                .title("title 1")
                                                .description("description")
                                                .content("content")
                                                .categoryId(1)
                                                .build(),
                                        PostDto.builder()
                                                .title("title 2")
                                                .description("description")
                                                .content("content")
                                                .categoryId(2)
                                                .build()
                                )
                        )
                        .pageNo(0)
                        .pageSize(2)
                        .totalElement(2)
                        .totalPage(1)
                        .last(true)
                        .build()
        );

        ResultActions response = mockMvc.perform(get("/api/posts")
                .header("Authorization", "Bearer some.jwt.token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)));

    }
}
