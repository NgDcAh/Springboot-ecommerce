package com.ecommerce.service;

import com.ecommerce.dto.AdminDto;
import com.ecommerce.model.Admin;

public interface AdminService {
	Admin findByUsername(String username);
	
	Admin save(AdminDto adminDto);
}
