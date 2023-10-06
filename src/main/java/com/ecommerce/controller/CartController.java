package com.ecommerce.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Customer;
import com.ecommerce.model.ShoppingCart;
import com.ecommerce.service.impl.CustomerServiceImpl;
import com.ecommerce.service.impl.ProductServiceImpl;
import com.ecommerce.service.impl.ShoppingCartServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	@Autowired
	private ShoppingCartServiceImpl cartServiceImpl;
	
	@Autowired
    private ProductServiceImpl productServiceImpl;
	
	@Autowired
    private CustomerServiceImpl customerServiceImpl;

	@GetMapping("/customer/cart")
    public String cart(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerServiceImpl.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            if (cart == null) {
                model.addAttribute("check");

            }
            if (cart != null) {
                model.addAttribute("grandTotal", cart.getTotalPrice());
                session.setAttribute("totalItems", cart.getTotalItems());
            }
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("title", "Cart");
            return "cart";
        }

    }

    @RequestMapping("/customer/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpServletRequest request,
                                Model model,
                                Principal principal,
                                HttpSession session) {


        ProductDto productDto = productServiceImpl.getById(id);
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            ShoppingCart shoppingCart = cartServiceImpl.addItemToCart(productDto, quantity, username);
            session.setAttribute("totalItems", shoppingCart.getTotalItems());
            model.addAttribute("shoppingCart", shoppingCart);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/customer/update-cart-product", method = RequestMethod.POST)
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("quantity") int quantity,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            ProductDto productDto = productServiceImpl.getById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = cartServiceImpl.updateCart(productDto, quantity, username);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/customer/cart";
        }

    }

    @RequestMapping(value = "/customer/delete-cart-product", method = RequestMethod.GET)
    public String deleteItem(@RequestParam("id") Long id,
                             Model model,
                             Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            ProductDto productDto = productServiceImpl.getById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = cartServiceImpl.removeItemFromCart(productDto, username);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/customer/cart";
        }
    }

}

