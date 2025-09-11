package com.demo.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.demo.model.Category;
import com.demo.repository.CategoryRepo;
import com.demo.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public Category saveCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryRepo.save(category);
	}

	@Override
	public List<Category> getAllCategory() {
		// TODO Auto-generated method stub
		
		return categoryRepo.findAll();
	}

	@Override
	public Boolean existCategory(String name) {
		// TODO Auto-generated method stub
		
		return categoryRepo.existsByName(name);
	}

	@Override
	public Boolean deleteCategory(int id) {
		// TODO Auto-generated method stub
		Category category=categoryRepo.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(category))
		{
			categoryRepo.delete(category);
			return true;
		}
		return false;
	}

	@Override
	public Category getCategoryById(int id) {
		// TODO Auto-generated method stub
		
		Category category = categoryRepo.findById(id).orElse(null);
		return category;
	}

	@Override
	public List<Category> getAllActiveCategory() {
		// TODO Auto-generated method stub
		List<Category> categories = categoryRepo.findByIsActiveTrue();
		return categories;
	}
	
	
	

}
