package com.ecommerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Category;
import com.ecommerce.service.impl.CategoryServiceImpl;
import com.ecommerce.service.impl.ProductServiceImpl;

@Controller
public class ProductController {
	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	
	@Autowired 
	private ProductServiceImpl productServiceImpl;
	

	@GetMapping("/admin/products")
    public String products(Model model) {
        List<ProductDto> products = productServiceImpl.allProduct();
        model.addAttribute("products", products);
        model.addAttribute("size", products.size());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "products";
    }
	

	// Pagination
	
    @GetMapping("/products/{pageNo}")
    public String allProducts(@PathVariable("pageNo") int pageNo, Model model) {   
        Page<ProductDto> products = productServiceImpl.getAllProducts(pageNo);
        List<CategoryDto> categories = categoryServiceImpl.getCategoriesAndSize();
        model.addAttribute("title", "Manage Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "shop";
    }
    
    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        ProductDto product = productServiceImpl.getById(id);
        List<ProductDto> productDtoList = productServiceImpl.randomProductLikeCategory(product.getCategory().getName(), product.getId());
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Product Detail");
        model.addAttribute("page", "Product Detail");
        model.addAttribute("productDetail", product);
        return "product-detail";
    }
    

	
    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
                                @RequestParam(value = "keyword") String keyword,
                                Model model
    ) {
        Page<ProductDto> products = productServiceImpl.searchProducts(pageNo, keyword);
        List<CategoryDto> categoryDtos = categoryServiceImpl.getCategoriesAndSize();
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "search-product";

    }
    
    @GetMapping("/find-products/{pageNo}")
    public String productsInCategory(@RequestParam("id") Long id, @PathVariable("pageNo") int pageNo, Model model) {
        List<CategoryDto> categoryDtos = categoryServiceImpl.getCategoriesAndSize();
        Page<ProductDto> products = productServiceImpl.findByCategoryId(pageNo, id);
        Category category = categoryServiceImpl.findById(id).orElse(null);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("categoryDto", category);
        //model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Product-Category");
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "products-in-category";
    }

    
    /*
    @GetMapping("/search-products")
    public String searchProduct(@RequestParam(value = "keyword") String keyword,
                                Model model) {
        List<ProductDto> products = productServiceImpl.searchProducts(keyword);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("products", products);
        return "product-result";

    }
    */

    @GetMapping("/admin/add-product")
    public String addProductPage(Model model) {
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryServiceImpl.findAllByActivatedTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", new ProductDto());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "add-product";
    }

    @PostMapping("/admin/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes redirectAttributes) {
        try {
            productServiceImpl.save(imageProduct, product);
            redirectAttributes.addFlashAttribute("success", "Add new product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        List<Category> categories = categoryServiceImpl.findAllByActivatedTrue();
        ProductDto productDto = productServiceImpl.getById(id);
        model.addAttribute("title", "Add Product");
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update-product";
    }

    @PostMapping("/admin/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes) {
        try {

            productServiceImpl.update(imageProduct, productDto);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/admin/products";
    }

    @RequestMapping(value = "/admin/enable-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(Long id, RedirectAttributes redirectAttributes) {
        try {
            productServiceImpl.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return "redirect:/admin/products";
    }

    @RequestMapping(value = "/admin/delete-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(Long id, RedirectAttributes redirectAttributes) {
        try {
            productServiceImpl.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/admin/products";
    }
    
    @GetMapping("/high-price/{pageNo}")
    public String filterHighPrice(@PathVariable("pageNo") int pageNo, Model model) {
        List<CategoryDto> categories = categoryServiceImpl.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        Page<ProductDto> products = productServiceImpl.filterHighProducts(pageNo);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "high-price";
    }


    @GetMapping("/lower-price/{pageNo}")
    public String filterLowerPrice(@PathVariable("pageNo") int pageNo, Model model) {
        List<CategoryDto> categories = categoryServiceImpl.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        Page<ProductDto> products = productServiceImpl.filterLowerProducts(pageNo);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "lower-price";
    }
}
