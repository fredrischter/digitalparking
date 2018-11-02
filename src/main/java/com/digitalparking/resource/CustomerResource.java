package com.digitalparking.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalparking.model.Customer;
import com.digitalparking.service.CustomerService;

@RestController("/pms/v1/assets")
public class CustomerResource {
	
	@Autowired
	CustomerService customerService; 

	@PostMapping("/{asset}/sessions")
	public void createSession(@Valid Customer customer) {
		customerService.create(customer);
	}
}
