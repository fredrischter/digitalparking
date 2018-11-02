package com.digitalparking.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalparking.model.WalletTopUp;
import com.digitalparking.service.CustomerService;

@RestController("/wallet")
public class WalletResource {
	
	@Autowired
	CustomerService customerService; 

	@PostMapping
	public void create(@Valid WalletTopUp wallet) {
		customerService.addMoney(wallet);
	}
}
