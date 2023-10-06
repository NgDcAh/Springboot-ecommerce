package com.ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category save(Category category) {
		Category categorySave = new Category(category.getName());
        return categoryRepository.save(categorySave);
	}

	@Override
	public Category update(Category category) {
		Category categoryUpdate = categoryRepository.findById(category.getId()).orElse(null);
        categoryUpdate.setName(category.getName());
        return categoryRepository.save(categoryUpdate);
	}

	@Override
	public List<Category> findAllByActivatedTrue() {
		return categoryRepository.findAllByActivatedTrue();
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Optional<Category> findById(Long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public void deleteById(Long id) {
		Category category = categoryRepository.findById(id).orElse(null);
        category.setActivated(false);
        category.setDeleted(true);
        categoryRepository.save(category);
		
	}

	@Override
	public void enableById(Long id) {
		Category category = categoryRepository.findById(id).orElse(null);
        category.setActivated(true);
        category.setDeleted(false);
        categoryRepository.save(category);
	}

	@Override
	public List<CategoryDto> getCategoriesAndSize() {
		List<CategoryDto> categories = categoryRepository.getCategoriesAndSize();
        return categories;
	}

}
