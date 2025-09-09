package com.demo.service;

import java.util.List;

import com.demo.model.Product;

public interface ProductService {

	public Product saveProduct(Product product);
	
	public List<Product> getAllProducts();
	
	public Boolean deleteProductById(int id);
}
