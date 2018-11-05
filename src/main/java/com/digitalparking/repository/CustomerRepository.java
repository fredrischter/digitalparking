package com.digitalparking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.digitalparking.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	@Query("select c from Customer c where c.email = ?1")
	Customer findByEmail(String email);

}
