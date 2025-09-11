package com.demo.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.demo.model.Product;
import com.demo.repository.ProductRepo;
import com.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepo productRepo;
	
	@Override
	public Product saveProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepo.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return productRepo.findAll();
	}

	@Override
	public Boolean deleteProductById(int id) {
		// TODO Auto-generated method stub
		 Product product = productRepo.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(product))
		{
			productRepo.delete(product);
			return true;
		}	 
		return false;
	}

	@Override
	public Product getProductById(int id) {
		Product product = productRepo.findById(id).orElse(null);
		
		
		return product;
	}

	@Override
	public Product updateProduct(Product product,MultipartFile image) {
		// TODO Auto-generated method stub
		 Product dbProduct = getProductById(product.getId());
		
		String imageName = image.isEmpty()? dbProduct.getImage():image.getOriginalFilename();
		
		dbProduct.setTitle(product.getTitle());
		dbProduct.setDescription(product.getDescription());
		dbProduct.setCategory(product.getCategory());
		dbProduct.setPrice(product.getPrice());
		dbProduct.setStock(product.getStock());
		dbProduct.setImage(imageName);
		dbProduct.setDiscount(product.getDiscount());
		dbProduct.setIsActive(product.getIsActive());
		
		
		Double discount=product.getPrice()*(product.getDiscount()/100.0);
		Double discountPrice=product.getPrice()-discount;
		dbProduct.setDiscountPrice(discountPrice);
		
		
		Product updateProduct = productRepo.save(dbProduct);
		
		if(!ObjectUtils.isEmpty(updateProduct))
		{
			if(!image.isEmpty())
			{
				
				try {
					File saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+image.getOriginalFilename());
					
					Files.copy(image.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
					}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return product;
		}
	//	return updateProduct;
		return null;
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		// TODO Auto-generated method stub
		List<Product> products = null;
		if(ObjectUtils.isEmpty(category))
		{
			products=productRepo.findByIsActiveTrue();
			
		}else
		{
			products=productRepo.findByCategory(category);
		}
		
	return products;
	}
		
	
}
