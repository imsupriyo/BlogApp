package com.springboot.blog.service;

import com.springboot.blog.payload.CategoryDTO;

import java.util.List;

public interface CategoryService {
    String deleteById(long categoryId);

    CategoryDTO updateById(long categoryId, CategoryDTO categoryDTO);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    CategoryDTO findById(long categoryId);
}
