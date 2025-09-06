package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	
	@GetMapping("/")
	public String indexPage()
	{
		return "admin/index";
	}
	
	
	@GetMapping("/loadAddProduct")
	public String loadAddProduct()
	{
		return "admin/add_product";
	}
	
	
	@GetMapping("/category")
	public String categoryPage()
	{
		return "admin/category";
	}
}
