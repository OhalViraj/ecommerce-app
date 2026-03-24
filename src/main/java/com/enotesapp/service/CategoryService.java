package com.enotesapp.service;

import java.util.List;

import com.enotesapp.entity.Category;

public interface CategoryService {

	public Boolean saveCategory(Category category);
	
	public List<Category> getAllCategory();
}
