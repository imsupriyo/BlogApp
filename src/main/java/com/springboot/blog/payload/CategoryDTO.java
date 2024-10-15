package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "CategoryDTO model information")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private long id;
    private String name;
    private String description;
}
