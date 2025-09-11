package com.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demo.model.Product;

public interface ProductService {

	public Product saveProduct(Product product);
	
	public List<Product> getAllProducts();
	
	public Boolean deleteProductById(int id);
	
	public Product getProductById(int id);
	
	public Product updateProduct(Product product,MultipartFile file);
	
	public List<Product> getAllActiveProducts(String category);
	
	
}
