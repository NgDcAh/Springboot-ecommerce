package com.ecommerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.dto.CustomerDto;
import com.ecommerce.model.City;
import com.ecommerce.model.Country;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.ShoppingCart;
import com.ecommerce.service.impl.CityServiceImpl;
import com.ecommerce.service.impl.CountryServiceImpl;
import com.ecommerce.service.impl.CustomerServiceImpl;
import com.ecommerce.service.impl.OrderServiceImpl;
import com.ecommerce.service.impl.ShoppingCartServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	@Autowired
	private CustomerServiceImpl customerServiceImpl;
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	@Autowired
	private ShoppingCartServiceImpl cartServiceImpl;
	@Autowired
	private CountryServiceImpl countryServiceImpl;
	@Autowired
	private CityServiceImpl cityServiceImpl;

	@GetMapping("/customer/check-out")
	public String checkOut(Principal principal, Model model) {
		if (principal == null) {
			return "redirect:/login";
		} else {
			CustomerDto customer = customerServiceImpl.getCustomer(principal.getName());
			if (customer.getAddress() == null || customer.getCity() == null || customer.getPhoneNumber() == null) {
				model.addAttribute("information", "You need update your information before check out");
				List<Country> countryList = countryServiceImpl.findAll();
				List<City> cities = cityServiceImpl.findAll();
				model.addAttribute("customer", customer);
				model.addAttribute("cities", cities);
				model.addAttribute("countries", countryList);
				model.addAttribute("title", "Profile");
				model.addAttribute("page", "Profile");
				return "profile";
			} else {
				ShoppingCart cart = customerServiceImpl.findByUsername(principal.getName()).getCart();
				model.addAttribute("customer", customer);
				model.addAttribute("title", "Check-Out");
				model.addAttribute("page", "Check-Out");
				model.addAttribute("shoppingCart", cart);
				model.addAttribute("grandTotal", cart.getTotalItems());
				return "checkout";
			}
		}
	}

	@GetMapping("/customer/orders")
	public String getOrders(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		} else {
			Customer customer = customerServiceImpl.findByUsername(principal.getName());
			List<Order> orderList = customer.getOrders();
			model.addAttribute("orders", orderList);
			model.addAttribute("title", "Order");
			model.addAttribute("page", "Order");
			return "order";
		}
	}

	@RequestMapping(value = "/customer/cancel-order", method = { RequestMethod.PUT, RequestMethod.GET })
	public String cancelOrder(@RequestParam("id") Long id, RedirectAttributes attributes) {
		orderServiceImpl.cancelOrder(id);
		attributes.addFlashAttribute("success", "Cancel order successfully!");
		return "redirect:/customer/orders";
	}

	@RequestMapping(value = "/customer/add-order", method = { RequestMethod.POST })
	public String createOrder(Principal principal, Model model, HttpSession session) {
		if (principal == null) {
			return "redirect:/login";
		} else {
			Customer customer = customerServiceImpl.findByUsername(principal.getName());
			ShoppingCart cart = customer.getCart();
			Order order = orderServiceImpl.save(cart);
			session.removeAttribute("totalItems");
			model.addAttribute("order", order);
			model.addAttribute("title", "Order Detail");
			model.addAttribute("page", "Order Detail");
			model.addAttribute("success", "Add order successfully");
			return "order-detail";
		}
	}
}
