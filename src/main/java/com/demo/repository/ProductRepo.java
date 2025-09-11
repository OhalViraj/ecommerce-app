package com.demo.repository;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{

	List<Product> findByIsActiveTrue();

	 List<Product> findByCategory(String category);
}
