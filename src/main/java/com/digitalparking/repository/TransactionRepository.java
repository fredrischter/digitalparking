package com.digitalparking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.digitalparking.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	@Query("select sum(t.value) from Transaction t where customerId=?1")
	Integer sumByCustomerId(Integer customerId);

}
	