package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    void deletePostById(long id);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    List<PostDto> getPostsByCategory(long categoryId);
}
