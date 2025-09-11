package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.model.Category;
import com.demo.model.Product;
import com.demo.repository.ProductRepo;
import com.demo.service.CategoryService;
import com.demo.service.ProductService;

@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public String indexPage()
	{
		return "index";
	}
	
	@GetMapping("/signin")
	public String loginPage()
	{
		return "login";
	}
	
	@GetMapping("/register")
	public String registerPage()
	{
		return "register";
	}
	
	@GetMapping("/products")
	public String productPage(Model m,@RequestParam(value ="category",defaultValue = "") String category)
	{
		List<Category> categories = categoryService.getAllActiveCategory();
		List<Product> products = productService.getAllActiveProducts(category);
		m.addAttribute("categories",categories);
		m.addAttribute("products",products);
		m.addAttribute("paramValue",category);
		return "product";
	}
	
	@GetMapping("/viewProduct/{id}")
	public String viewproductPage(@PathVariable int id,Model m)
	{
		Product productById = productService.getProductById(id);
		m.addAttribute("product",productById);
		return "view_product";
	}
}
