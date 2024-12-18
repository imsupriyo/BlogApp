package com.springboot.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot Blog App REST APIs",
                description = "Spring Boot Blog App REST APIs documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Supriyo",
                        email = "supriyopal388@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Blog App REST APIs documentation",
                url = "https://github.com/imsupriyo/blog-rest-api")
)
public class BlogRestApiApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogRestApiApplication.class, args);
    }

}
