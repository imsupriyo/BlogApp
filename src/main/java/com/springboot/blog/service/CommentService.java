package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO addComment(long postId, CommentDTO commentDTO);

    List<CommentDTO> findCommentsByPostId(long postId);

    CommentDTO findByCommentId(long postId, long commentId);

    CommentDTO updateByCommentId(long postId, long commentId, CommentDTO commentDTO);

    void deleteByCommentId(long postId, long commentId);
}
