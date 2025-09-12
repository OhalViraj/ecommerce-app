package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.model.Category;
import com.demo.model.Product;
import com.demo.model.UserDtls;
import com.demo.repository.ProductRepo;
import com.demo.service.CategoryService;
import com.demo.service.ProductService;
import com.demo.service.UserDtlsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserDtlsService userDtlsService;
	
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
	
	@PostMapping("/saveUser")
	public String saveUserDtls(@ModelAttribute UserDtls userDtls,@RequestParam("img") MultipartFile file,HttpSession session) throws IOException
	{
		String imageName=file.isEmpty()?"default.jpg":file.getOriginalFilename();
		userDtls.setProfileImage(imageName);
		UserDtls saveUse = userDtlsService.saveUserDtls(userDtls);
		
		if(!ObjectUtils.isEmpty(saveUse))
		{
			if(!file.isEmpty())
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator
						+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				session.setAttribute("succMsg", "File Saved Successfully");
			
			}
				
		}else
		{
			session.setAttribute("errorMsg", "Something Wrong on Server");	
		}
		
		return "redirect:/register";
	}
}
