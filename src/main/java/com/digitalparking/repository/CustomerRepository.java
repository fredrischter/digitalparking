package com.digitalparking.repository;

import org.springframework.data.repository.CrudRepository;

import com.digitalparking.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
