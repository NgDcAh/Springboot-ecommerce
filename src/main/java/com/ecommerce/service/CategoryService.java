package com.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.model.Category;

public interface CategoryService {

	Category save(Category category);

    Category update(Category category);

    List<Category> findAllByActivatedTrue();

    List<Category> findAll();

    Optional<Category> findById(Long id);

    void deleteById(Long id);

    void enableById(Long id);

    List<CategoryDto> getCategoriesAndSize();
}
