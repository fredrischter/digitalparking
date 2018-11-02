package com.digitalparking.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalparking.model.Vehicle;
import com.digitalparking.service.CustomerService;

@RestController("/vehicle")
public class VehicleResource {
	
	@Autowired
	CustomerService customerService; 

	@PostMapping
	public void create(@Valid Vehicle vehicle) {
		customerService.addVehicle(vehicle);
	}
}
