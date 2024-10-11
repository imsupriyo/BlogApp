package com.springboot.blog.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {

    public static void main(String[] args) {
        org.springframework.security.crypto.password.PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("test"));
        System.out.println(encoder.encode("admin"));
    }
}
