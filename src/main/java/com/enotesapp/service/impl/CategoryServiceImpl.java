package com.enotesapp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotesapp.entity.Category;
import com.enotesapp.repository.CategoryRepository;
import com.enotesapp.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepo;
	
	
	@Override
	public Boolean saveCategory(Category category) {
		// TODO Auto-generated method stub
		
		category.setIsDeleted(false);
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());
	
		Category saveCategory=categoryRepo.save(category);
		
		if(ObjectUtils.isEmpty(saveCategory))
		{
			return false;
		}
		
		return true;
	}

	@Override
	public List<Category> getAllCategory() {
		// TODO Auto-generated method stub
		
		List<Category> categories=categoryRepo.findAll();
		return categories;
	}

}
