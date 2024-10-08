package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId,
                                                    @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.addComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public List<CommentDTO> findCommentsByPostId(@PathVariable(value = "postId") long postId) {
        return commentService.findCommentsByPostId(postId);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> findByCommentId(@PathVariable("postId") long postId,
                                      @PathVariable("commentId") long commentId) {
        return ResponseEntity.ok(commentService.findByCommentId(postId, commentId));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateByCommentId(@PathVariable("postId") long postId,
                                                      @PathVariable("commentId") long commentId,
                                                        @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateByCommentId(postId, commentId, commentDTO));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteByCommentId(@PathVariable("postId") long postId,
                                                    @PathVariable("commentId") long commentId) {
        commentService.deleteByCommentId(postId, commentId);
        return ResponseEntity.ok(String.format("Comment with id = %d is deleted", commentId));
    }
}
