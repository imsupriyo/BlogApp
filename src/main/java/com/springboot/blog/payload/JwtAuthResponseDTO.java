package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "JwtAuthResponse model information")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponseDTO {
    @Schema(description = "Jwt access token")
    private String accessToken;

    @Schema(description = "JWT token type")
    private String tokenType = "Bearer";
}
