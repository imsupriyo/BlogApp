package com.springboot.blog.repository;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CommentRepositoryTests {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    private Long postId;

    @BeforeEach
    public void setup() {
        Post post = new Post();
        post.setTitle("Post 1");
        post.setDescription("Post Description");
        post.setContent("Post Content");

        Comment comment1 = new Comment();
        comment1.setName("user1");
        comment1.setEmail("user1@gmail.com");
        comment1.setMessage("very information post");
        comment1.setPost(post);

        Comment comment2 = new Comment();
        comment2.setName("user2");
        comment2.setEmail("user2@gmail.com");
        comment2.setMessage("very helpful post");
        comment2.setPost(post);

        post.getComments().add(comment1);
        post.getComments().add(comment2);

        Category category = new Category();
        category.setName("sci-fi");
        category.setDescription("science fiction");
        category.setPosts(Set.of(post));

        post.setCategory(category);

        Post savedPost = postRepository.save(post);
        postId = savedPost.getId();

        assertThat(postId).isGreaterThanOrEqualTo(1L);
    }

    @DisplayName("test to find list of Comments by Post id")
    @Test
    public void givenPostObject_whenFindByPostId_thenReturnListOfPosts() {
        List<Comment> comments = commentRepository.findByPostId(postId);
        System.out.println(comments);

        assertThat(comments).isNotNull();
        assertThat(comments.size()).isEqualTo(2);
    }
}
