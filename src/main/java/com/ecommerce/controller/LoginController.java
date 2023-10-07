package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecommerce.dto.AdminDto;
import com.ecommerce.dto.CustomerDto;
import com.ecommerce.model.Admin;
import com.ecommerce.model.Customer;
import com.ecommerce.service.impl.AdminServiceImpl;
import com.ecommerce.service.impl.CustomerServiceImpl;

import jakarta.validation.Valid;


@Controller
public class LoginController {
	
	@Autowired
	private AdminServiceImpl adminServiceImpl;
	
	@Autowired
	private CustomerServiceImpl customerServiceImpl;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	

	@RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }
	
	
	@RequestMapping("/admin/index")
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "index";
    }
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("customerDto", new CustomerDto());
		return "register";
	}
	
//	@GetMapping("/register")
//	public String register(Model model) {
//		model.addAttribute("adminDto", new AdminDto());
//		return "register";
//	}
	
	@GetMapping("/forgot-password")
	public String forgotPassword(Model model) {
		return "forgot-password";
	}
	
	@PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                              BindingResult result,
                              Model model) {

        try {

            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                result.toString();
                return "register";
            }
            String username = customerDto.getUsername();
            Customer customer = customerServiceImpl.findByUsername(username);
            if (customer != null) {
                model.addAttribute("customerDto", customerDto);
                System.out.println("customer not null");
                model.addAttribute("emailError", "Your email has been registered!");
                return "register";
            }
            if (customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
            	customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerServiceImpl.save(customerDto);
                System.out.println("success");
                model.addAttribute("success", "Register successfully!");
                model.addAttribute("customerDto", customerDto);
            } else {
                model.addAttribute("customerDto", customerDto);
                model.addAttribute("passwordError", "Your password maybe wrong! Check again!");
                System.out.println("password not same");
                return "register";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errors", "The server has been wrong!");
        }
        return "register";

    }
	
//	@PostMapping("/register-new")
//    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
//                              BindingResult result,
//                              Model model) {
//
//        try {
//
//            if (result.hasErrors()) {
//                model.addAttribute("adminDto", adminDto);
//                result.toString();
//                return "register";
//            }
//            String username = adminDto.getUsername();
//            Admin admin = adminServiceImpl.findByUsername(username);
//            if (admin != null) {
//                model.addAttribute("adminDto", adminDto);
//                System.out.println("admin not null");
//                model.addAttribute("emailError", "Your email has been registered!");
//                return "register";
//            }
//            if (adminDto.getPassword().equals(adminDto.getRepeatPassword())) {
//            	adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
//                adminServiceImpl.save(adminDto);
//                System.out.println("success");
//                model.addAttribute("success", "Register successfully!");
//                model.addAttribute("adminDto", adminDto);
//            } else {
//                model.addAttribute("adminDto", adminDto);
//                model.addAttribute("passwordError", "Your password maybe wrong! Check again!");
//                System.out.println("password not same");
//                return "register";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("errors", "The server has been wrong!");
//        }
//        return "register";
//
//    }
}
