package com.springboot.blog.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Login DTO model information")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @Schema(description = "username or email")
    private String usernameOrEmail;

    @Schema(description = "password")
    private String password;
}
