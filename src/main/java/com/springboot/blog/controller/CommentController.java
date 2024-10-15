package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CRUD REST APIs for Comment Resource")
@RestController
@RequestMapping("/api/posts")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Create Comment REST API",
            description = "Create Comment REST API is used to add a comment to a post"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId,
                                                    @Valid @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.addComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Find Comments By Post Id REST API",
            description = "Find Comments By Post Id REST API is used to fetch "
                    + "all the comments associated with a post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{postId}/comments")
    public List<CommentDTO> findCommentsByPostId(@PathVariable(value = "postId") long postId) {
        return commentService.findCommentsByPostId(postId);
    }

    @Operation(
            summary = "Find By Comment Id REST API",
            description = "Find By Comment Id REST API is used to fetch "
                    + "a comments from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> findByCommentId(@PathVariable("postId") long postId,
                                      @PathVariable("commentId") long commentId) {
        return ResponseEntity.ok(commentService.findByCommentId(postId, commentId));
    }

    @Operation(
            summary = "Update Comment REST API",
            description = "Update Comment REST API is used to update "
                    + "a comment and store it into the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateByCommentId(@PathVariable("postId") long postId,
                                                      @PathVariable("commentId") long commentId,
                                                       @Valid @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateByCommentId(postId, commentId, commentDTO));
    }

    @Operation(
            summary = "Delete Comment REST API",
            description = "Delete Comments REST API is used to delete "
                    + "a comment from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteByCommentId(@PathVariable("postId") long postId,
                                                    @PathVariable("commentId") long commentId) {
        commentService.deleteByCommentId(postId, commentId);
        return ResponseEntity.ok(String.format("Comment with id = %d is deleted", commentId));
    }
}
