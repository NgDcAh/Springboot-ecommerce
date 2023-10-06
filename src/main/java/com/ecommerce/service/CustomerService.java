package com.ecommerce.service;

import com.ecommerce.dto.CustomerDto;
import com.ecommerce.model.Customer;

public interface CustomerService {
	Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer update(CustomerDto customerDto);

    Customer changePass(CustomerDto customerDto);

    CustomerDto getCustomer(String username);
}
