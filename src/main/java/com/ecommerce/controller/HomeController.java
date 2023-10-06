package com.ecommerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Customer;
import com.ecommerce.model.ShoppingCart;
import com.ecommerce.service.impl.CustomerServiceImpl;
import com.ecommerce.service.impl.ProductServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private CustomerServiceImpl customerServiceImpl;
	
	@Autowired 
	private ProductServiceImpl productServiceImpl;
	

	@RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "Home");
        model.addAttribute("page", "Home");
        List<ProductDto> products = productServiceImpl.randomProduct();
        model.addAttribute("products", products);
        if (principal != null) {
            Customer customer = customerServiceImpl.findByUsername(principal.getName());
            session.setAttribute("username", customer.getFirstName() + " " + customer.getLastName());
            ShoppingCart shoppingCart = customer.getCart();
            if (shoppingCart != null) {
                session.setAttribute("totalItems", shoppingCart.getTotalItems());
            }
        }
        return "home";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact");
        model.addAttribute("page", "Contact");
        return "contact-us";
    }
}
