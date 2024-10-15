package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "CommentDTO model information")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long id;

    @Schema(description = "username")
    @NotEmpty
    private String name;

    @Schema(description = "email address")
    @Email(message = "Not a valid email address")
    private String email;

    @Schema(description = "Comment message")
    @NotEmpty
    @Size(min = 5, message = "message should have 10 characters")
    private String message;
}
