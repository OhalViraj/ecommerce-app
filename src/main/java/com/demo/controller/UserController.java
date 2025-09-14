package com.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.model.Category;
import com.demo.model.UserDtls;
import com.demo.service.CategoryService;
import com.demo.service.UserDtlsService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CategoryService categoryService;
	
	
	@Autowired
	private UserDtlsService userDtlsService;

	@ModelAttribute
	public void getUserDetails(Principal p,Model m)
	{
		if(p!=null)
		{
			String email=p.getName();
			UserDtls userByDtls= userDtlsService.getUserByEmail(email);
			m.addAttribute("user",userByDtls);
		}
		

		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys",allActiveCategory);
	
	}

	
	@GetMapping("/")
	public String userHomePage()
	{
		return "user/home";
	}
	
}
