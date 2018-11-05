package com.digitalparking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalparking.model.Customer;
import com.digitalparking.repository.CustomerRepository;
import com.digitalparking.repository.TransactionRepository;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	TransactionRepository transactionRepository;

	public void create(Customer customer) {
		customerRepository.save(customer);
	}
	
	public Integer getBalance(Integer customerId) {
		return transactionRepository.sumByCustomerId(customerId);
	}
	
	public Integer getBalance(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return transactionRepository.sumByCustomerId(customer.getId());
	}

}
