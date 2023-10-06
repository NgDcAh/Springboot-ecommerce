package com.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Country;
import com.ecommerce.repository.CountryRepository;
import com.ecommerce.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService{
	@Autowired
	private CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
