package com.springboot.blog.controller;

import com.springboot.blog.payload.JwtAuthResponseDTO;
import com.springboot.blog.payload.LoginDTO;
import com.springboot.blog.payload.RegisterDTO;
import com.springboot.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "REST APIs for Authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Log In REST API",
            description = "Log In REST API is used to logged into the Blog Application "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JwtAuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        JwtAuthResponseDTO responseDTO = new JwtAuthResponseDTO();
        responseDTO.setAccessToken(token);

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Register REST API",
            description = "Register REST API is used to register a new user and store it in the database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(authService.register(registerDTO), HttpStatus.CREATED);
    }
}
