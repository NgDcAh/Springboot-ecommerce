package com.ecommerce.dto;

import java.util.Set;

import com.ecommerce.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {

	private Long id;

    private Customer customer;

    private double totalPrice;

    private int totalItems;

    private Set<CartItemDto> cartItems;

}
