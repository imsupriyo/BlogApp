package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Schema(description = "PostDTO model information")
@Data
public class PostDto {
    private long id;

    @Schema(description = "Blog Post title")
    @NotEmpty
    @Size(min = 2, message = "post title should have 2 characters")
    private String title;

    @Schema(description = "Blog Post description")
    @NotEmpty
    @Size(min = 10, message = "post title should have 10 characters")
    private String description;

    @Schema(description = "Blog Post content")
    @NotEmpty
    private String content;

    @Schema(description = "Blog Post comments")
    private Set<CommentDTO> comments;

    @Schema(description = "Blog Post category")
    private long categoryId;
}