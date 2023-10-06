package com.ecommerce.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.AdminDto;
import com.ecommerce.model.Admin;
import com.ecommerce.repository.AdminRepository;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired 
	private AdminRepository adminRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Admin findByUsername(String username) {
		return adminRepository.findByUsername(username);
	}

	@Override
	public Admin save(AdminDto adminDto) {
		Admin admin = new Admin();
		admin.setFirstName(adminDto.getFirstName());
		admin.setLastName(adminDto.getLastName());
		admin.setUsername(adminDto.getUsername());
		admin.setPassword(adminDto.getPassword());
		admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
		return adminRepository.save(admin);
	}

}
