package com.demo.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
