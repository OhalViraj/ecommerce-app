package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepo productRepo;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
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


    AdminController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }
	
	@GetMapping("/")
	public String indexPage()
	{
		return "admin/index";
	}
	
	
	
	@GetMapping("/category")
	public String categoryPage(Model m)
	{
		m.addAttribute("categorys",categoryService.getAllCategory());
		
		return "admin/category";
	}
	
	
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category,@RequestParam("file") MultipartFile file,HttpSession session) throws IOException
	{
		
		String imageName= file!=null?file.getOriginalFilename():"default.jpg";
		
			category.setImageName(imageName);
		Boolean existCategory=categoryService.existCategory(category.getName());
		
		if(existCategory)
		{
			session.setAttribute("errorMsg", "Category Name Alredy Exists");
		}else
		{
			Category saveCategory=categoryService.saveCategory(category);
			if(ObjectUtils.isEmpty(saveCategory))
			{
			session.setAttribute("errorMsg", "Not Save ! Internal Server Error");	
			}else
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				
				session.setAttribute("succMsg", "Save Success");	
			}
			
		}
		return "redirect:/admin/category";
	}
	
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id,HttpSession session)
	{
		Boolean deleteCategory = categoryService.deleteCategory(id);
	if(deleteCategory)
	{
		session.setAttribute("succMsg", "Deleted Successfully");
	}
	else {
		session.setAttribute("errorMsg", "Something Wrong On Server");
		
	}
		return "redirect:/admin/category";
		
	}
	
	@GetMapping("/loadEditCategory/{id}")
	public String loadEditCategory(@PathVariable int id,Model m,HttpSession session)
	{
		m.addAttribute("category",categoryService.getCategoryById(id));
		
		
		return "admin/edit_category";
	}
	
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category,@RequestParam("file") MultipartFile file,HttpSession session) throws IOException
	{
		Category oldCategory = categoryService.getCategoryById(category.getId());
		
		String imageName =file.isEmpty() ? oldCategory.getImageName():file.getOriginalFilename();
		
		if(!ObjectUtils.isEmpty(category))
		{
			oldCategory.setName(category.getName());
			oldCategory.setIsActive(category.getIsActive());
			oldCategory.setImageName(imageName);
		}
		
		Category updateCategory = categoryService.saveCategory(oldCategory);
	
		if(!ObjectUtils.isEmpty(updateCategory))
		{
			if(!file.isEmpty())
			{
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
		
			}
			
			session.setAttribute("succMsg", "Update Successfully");
		}else
		{
			session.setAttribute("errorMsg", "Something Wrong");
				
		}
		
		return "redirect:/admin/loadEditCategory/"+category.getId();
	}
	

	
	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model m)
	{
		List<Category> categories = categoryService.getAllCategory();
		m.addAttribute("categories",categories);
		return "admin/add_product";
	}
	
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image,HttpSession session) throws IOException
	{
		String imageName=image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
		product.setImage(imageName);
		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());
		Product saveProduct = productService.saveProduct(product);
		
		if(!ObjectUtils.isEmpty(saveProduct))
		{
			File saveFile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+image.getOriginalFilename());
			
			Files.copy(image.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			session.setAttribute("succMsg", "Product Saves Successfully");
		}else
		{
			session.setAttribute("errorMsg","Something Wrong On Server");
		}
		
		return "redirect:/admin/loadAddProduct";
	}
	
	@GetMapping("/products")
	public String loadViewProducts(Model m)
	{
		List<Product> allProducts = productService.getAllProducts();
		m.addAttribute("products", allProducts);
		return "admin/products";
	}
	
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@ModelAttribute Product product,@PathVariable int id,HttpSession session)
	{
		Boolean deleteProductById = productService.deleteProductById(id);
		if(deleteProductById)
		{
			session.setAttribute("succMsg", "Product Successfully Deleted");
		}else
		{
			session.setAttribute("errorMsg", "Product Not Deleted ! Something Wrong On Server");
		}
		
		return "redirect:/admin/products";
	}
	
	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable int id,Model m)
	{

		m.addAttribute("product",productService.getProductById(id));
		m.addAttribute("categories",categoryService.getAllCategory());
		return "admin/edit_product";
	}
	
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product,HttpSession session,@RequestParam("file") MultipartFile image)
	{
		if(product.getDiscount()<0 || product.getDiscount()>100)
		{
			session.setAttribute("errorMsg", "Invalid Discount");
		}
		else
		{
			
	
		
		Product updateProduct = productService.updateProduct(product, image);
		
		if(!ObjectUtils.isEmpty(updateProduct))
		 {
			session.setAttribute("succMsg", "Product Updated Successfully");
			}else
			{
				session.setAttribute("errorMsg", "Something Wrong On Server");
			}
		}
		
		return "redirect:/admin/editProduct/"+product.getId();
	}
	
	@GetMapping("/users")
	public String getAllUsers(Model m)
	{
		List<UserDtls> users = userDtlsService.getUsers("ROLE_USER");
		m.addAttribute("users", users);
		return "/admin/users";
	}
	
	@GetMapping("/updateStatus")
	public String updateUserAccountStatus(@RequestParam Boolean status,@RequestParam Integer id,HttpSession session)
	{
		Boolean f=userDtlsService.updateAccountStatus(id,status);
		if(f)
		{
			session.setAttribute("succMsg", "Account status is updated");
		}
		else
		{
			session.setAttribute("errMsg", "Something Wrong On Server");
		}
		
		
		return "redirect:/admin/users";
		
	}
}
