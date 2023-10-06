package com.ecommerce.service;

import java.util.List;

import com.ecommerce.model.Order;
import com.ecommerce.model.ShoppingCart;

public interface OrderService {
	Order save(ShoppingCart shoppingCart);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order acceptOrder(Long id);

    void cancelOrder(Long id);
}
