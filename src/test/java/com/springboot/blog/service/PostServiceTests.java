package com.springboot.blog.service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private PostServiceImpl postService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        postService = new PostServiceImpl(postRepository, new ModelMapper(), categoryRepository);
    }

    @DisplayName("test to save a post")
    @Test
    public void givenPostDTO_whenCreatePost_thenSavedPostDTO() {
        PostDto postDto = PostDto.builder()
                .title("post title")
                .content("post content")
                .description("some description")
                .categoryId(1L)
                .build();

        Category category = new Category(1L, "sci-fi", "science-fiction", new HashSet<>());
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> {
                    Post savedPost = invocation.getArgument(0);
                    savedPost.setId(1L);  // Simulate saving and generating an ID
                    return savedPost;
                });

        PostDto postDto1 = postService.createPost(postDto);
        System.out.println(postDto1);

        assertThat(postDto1).isNotNull();
        assertThat(postDto1.getId()).isEqualTo(1L);
    }

    @DisplayName("test to retrieve all the posts")
    @Test
    public void givenPost_whenGetAllPosts_thenReturnListOfPosts() {
        List<Post> posts = List.of(
                new Post(1L, "title 1", "description 1", "content 1", null, null),
                new Post(2L, "title 2", "description 2", "content 2", null, null)
        );
        when(postRepository.findAll(PageRequest.of(0, 5, Sort.by("id").ascending())))
                .thenReturn(new PageImpl<>(posts));

        PostResponse allPosts = postService.getAllPosts(0, 5, "id", "asc");
        System.out.println(allPosts);

        assertThat(allPosts.getContent().size()).isEqualTo(2);
        assertThat(allPosts.getTotalElement()).isEqualTo(2);
        assertThat(allPosts.getPageSize()).isEqualTo(2);
    }

    @DisplayName("test to get a post by Post Id")
    @Test
    public void givenPostId_whenGetPostById_thenReturnAssociatedPostDto() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(
                        new Post(1L, "title 1", "description 1", "content 1", null, null))
                );
        PostDto post = postService.getPostById(1L);

        assertThat(post).isNotNull();
    }

    @DisplayName("test to update a post by Post Id")
    @Test
    public void givenPostId_whenUpdatePost_thenReturnUpdatePostDto() {
        Category category = new Category(1L, "sci-fi", "science-fiction", new HashSet<>());
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(
                        new Post(1L, "title 1", "description 1", "content 1", null, category))
                );

        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post updatedPost = invocation.getArgument(0);
            updatedPost.setTitle("updated title");
            updatedPost.setDescription("updated description");
            updatedPost.setContent("updated content");
            return updatedPost;
        });

        PostDto post = postService.updatePost(
                PostDto
                        .builder()
                        .title("updated title")
                        .description("updated description")
                        .content("updated content")
                        .categoryId(1L)
                        .build(),
                1L);

        assertThat(post).isNotNull();
        System.out.println(post);
    }

    @DisplayName("test to update a post by Invalid Post Id")
    @Test
    public void givenPostWithInvalidPostId_whenUpdatePost_thenThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(
                PostDto
                        .builder()
                        .title("post title")
                        .content("post content")
                        .description("some description")
                        .categoryId(1L)
                        .build(),
                0L));
    }

    @DisplayName("test to update a post by invalid Category Id")
    @Test
    public void givenPostWithInvalidCategoryId_whenUpdatePost_thenThrowException() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(new Post(1L, "title 1", "description 1", "content 1", null, null)));
        assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(
                PostDto
                        .builder()
                        .title("post title")
                        .content("post content")
                        .description("some description")
                        .categoryId(0L)
                        .build(),
                1L));
    }

    @DisplayName("test to retrieve list of post by Category Id")
    @Test
    public void givenCategoryId_whenGetPostsByCategory_thenReturnListOfPostDto() {
        Category category = new Category(1L, "sci-fi", "science-fiction", new HashSet<>());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(postRepository.findPostByCategoryId(1L))
                .thenReturn(
                        List.of(
                                new Post(1L, "title 1", "description 1", "content 1", null, category),
                                new Post(2L, "title 2", "description 2", "content 2", null, category)
                        ));

        List<PostDto> postDtos = postService.getPostsByCategory(1L);
        assertThat(postDtos).isNotNull();
        assertThat(postDtos.size()).isEqualTo(2);
        System.out.println(postDtos);
    }

    @DisplayName("test to throw exception when invalid Category Id is passed")
    @Test
    public void givenInvalidCategoryId_whenGetPostsByCategory_thenThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByCategory(1L));
    }

}
