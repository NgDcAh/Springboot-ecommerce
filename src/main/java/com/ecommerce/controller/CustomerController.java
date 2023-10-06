package com.ecommerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.dto.CustomerDto;
import com.ecommerce.model.City;
import com.ecommerce.model.Country;
import com.ecommerce.service.impl.CityServiceImpl;
import com.ecommerce.service.impl.CountryServiceImpl;
import com.ecommerce.service.impl.CustomerServiceImpl;

import jakarta.validation.Valid;

@Controller
public class CustomerController {
	@Autowired
	private CustomerServiceImpl customerServiceImpl;
	
	@Autowired
    private CountryServiceImpl countryServiceImpl;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private CityServiceImpl cityServiceImpl;

    @GetMapping("/customer/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            CustomerDto customer = customerServiceImpl.getCustomer(username);
            List<Country> countryList = countryServiceImpl.findAll();
            List<City> cities = cityServiceImpl.findAll();
            model.addAttribute("customer", customer);
            model.addAttribute("cities", cities);
            model.addAttribute("countries", countryList);
            model.addAttribute("title", "Profile");
            model.addAttribute("page", "Profile");
            return "profile";
        }
    }

    @PostMapping("/customer/update-profile")
    public String updateProfile(@Valid @ModelAttribute("customer") CustomerDto customerDto,
                                BindingResult result,
                                RedirectAttributes attributes,
                                Model model,
                                Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            CustomerDto customer = customerServiceImpl.getCustomer(username);
            List<Country> countryList = countryServiceImpl.findAll();
            List<City> cities = cityServiceImpl.findAll();
            model.addAttribute("countries", countryList);
            model.addAttribute("cities", cities);
            if (result.hasErrors()) {
                return "profile";
            }
            customerServiceImpl.update(customerDto);
            CustomerDto customerUpdate = customerServiceImpl.getCustomer(principal.getName());
            attributes.addFlashAttribute("success", "Update successfully!");
            model.addAttribute("customer", customerUpdate);
            return "redirect:/customer/profile";
        }
    }

    @GetMapping("/customer/change-password")
    public String changePassword(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("title", "Change password");
        model.addAttribute("page", "Change password");
        return "change-password";
    }

    @PostMapping("/customer/change-password")
    public String changePass(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("repeatNewPassword") String repeatPassword,
                             RedirectAttributes attributes,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            CustomerDto customer = customerServiceImpl.getCustomer(principal.getName());
            if (passwordEncoder.matches(oldPassword, customer.getPassword())
                    && !passwordEncoder.matches(newPassword, oldPassword)
                    && !passwordEncoder.matches(newPassword, customer.getPassword())
                    && repeatPassword.equals(newPassword) && newPassword.length() >= 5) {
                customer.setPassword(passwordEncoder.encode(newPassword));
                customerServiceImpl.changePass(customer);
                attributes.addFlashAttribute("success", "Your password has been changed successfully!");
                return "redirect:/customer/profile";
            } else {
                model.addAttribute("message", "Your password is wrong");
                return "change-password";
            }
        }
    }
}
