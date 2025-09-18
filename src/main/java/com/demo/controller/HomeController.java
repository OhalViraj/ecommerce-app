package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.demo.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {


	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserDtlsService userDtlsService;

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
				/*
				 * File saveFile = new ClassPathResource("static/img").getFile(); Path path =
				 * Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.
				 * separator +file.getOriginalFilename());
				 * Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				 */
				File saveFile = new ClassPathResource("static/img").getFile();
				File profileDir = new File(saveFile, "profile_img");
				if (!profileDir.exists()) {
				    profileDir.mkdirs();
				}
				Path path = Paths.get(profileDir.getAbsolutePath(), file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				
				session.setAttribute("succMsg", "File Saved Successfully");
			
			}
				
		}else
		{
			session.setAttribute("errorMsg", "Something Wrong on Server");	
		}
		
		return "redirect:/register";
	}
	
	
	//Forget Password
	
	@GetMapping("/forgetPassword")
	public String showForgetPassword()
	{
		return "forget_password";
	}

	@PostMapping("/forgetPass")
	public String processForgetPassword(@RequestParam String email,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException
	{
		UserDtls userByEmail = userDtlsService.getUserByEmail(email);
		
		if(ObjectUtils.isEmpty(userByEmail))
		{
			session.setAttribute("errorMsg","Invalid Email");
		}
		else
		{
			
			String resetToken = UUID.randomUUID().toString();
			userDtlsService.updateUserResetToken(email,resetToken);
		
			//Generate Url : http://localhost:8080/resetPassword?token=fguvvvcvv
			
			String url=CommonUtil.generateUrl(request)+"/resetPassword?token="+resetToken;
			
			Boolean sendMail = commonUtil.sendMail(url,email);
			if(sendMail)
			{
				session.setAttribute("succMsg","Please Check Your Mail.. Password Reset Link Sent");
				
			}else
			{
				session.setAttribute("errorMsg","Something Wrong On Server");
				
			}
		}
		return "redirect:/forgetPassword";
	}

	
	
	

	@GetMapping("/resetPassword")
	public String showResetPassword(@RequestParam String token,HttpSession session,Model m)
	{
		UserDtls userByToken = userDtlsService.getUserByToken(token);
		if(ObjectUtils.isEmpty(userByToken))
		{
			m.addAttribute("msg","Your Link is Invalid");
			
		 	return "message";
		}
		
		m.addAttribute("token",token);
		return "reset_password";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String token,Model m,@RequestParam String password,HttpSession session)
	{
		UserDtls userByToken = userDtlsService.getUserByToken(token);
		if(ObjectUtils.isEmpty(userByToken))
		{
			m.addAttribute("msg","Your Link is Invalid");
		 	return "message";
		}else
		{
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userDtlsService.updateUserDtls(userByToken);
			session.setAttribute("succMsg","Password Change Succesfully");
			
			m.addAttribute("msg","Password Change Succesfully");
			
			return "message";
		}
		
		
	}
}
