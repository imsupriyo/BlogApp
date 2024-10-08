package com.springboot.blog.payload;

import com.springboot.blog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long id;
    private String name;
    private String email;
    private String message;
}
