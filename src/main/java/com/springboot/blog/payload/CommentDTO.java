package com.springboot.blog.payload;

import com.springboot.blog.entity.Post;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long id;

    @NotEmpty
    private String name;

    @Email(message = "Not a valid email address")
    private String email;

    @NotEmpty
    @Size(min = 5, message = "message should have 10 characters")
    private String message;
}
