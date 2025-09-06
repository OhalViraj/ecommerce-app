package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	
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
	public String productPage()
	{
		return "product";
	}
	
	@GetMapping("/viewProduct")
	public String viewproductPage()
	{
		return "view_product";
	}
}
