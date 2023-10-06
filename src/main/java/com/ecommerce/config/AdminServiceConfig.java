package com.ecommerce.config;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecommerce.model.Admin;
import com.ecommerce.repository.AdminRepository;

public class AdminServiceConfig implements UserDetailsService{

	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByUsername(username);
		if (admin == null) {
			throw new UsernameNotFoundException("Could not found username");
		}
		return new User(
				admin.getUsername(),
				admin.getPassword(),
				admin.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
	}
}