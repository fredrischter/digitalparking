package com.digitalparking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalparking.model.Customer;
import com.digitalparking.model.Transaction;
import com.digitalparking.model.Vehicle;
import com.digitalparking.model.WalletTopUp;
import com.digitalparking.repository.CustomerRepository;
import com.digitalparking.repository.TransactionRepository;
import com.digitalparking.repository.VehicleRepository;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	TransactionRepository transactionRepository;

	public void create(Customer customer) {
		customerRepository.save(customer);
	}

	public void addMoney(WalletTopUp walletTopUp) {
		Transaction transaction = Transaction.builder().build();
		transactionRepository.save(transaction);
	}
	
	public Integer getBalance(Integer customerId) {
		return transactionRepository.sumByCustomerId(customerId);
	}

}
