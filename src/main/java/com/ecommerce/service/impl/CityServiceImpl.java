package com.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.City;
import com.ecommerce.repository.CityRepository;
import com.ecommerce.service.CityService;

@Service
public class CityServiceImpl implements CityService{

	@Autowired
	private CityRepository cityRepository;

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }
}
