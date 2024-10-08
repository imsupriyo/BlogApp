package com.springboot.blog.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BlogAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public BlogAPIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
