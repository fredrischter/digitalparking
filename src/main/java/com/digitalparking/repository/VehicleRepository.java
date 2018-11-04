package com.digitalparking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.digitalparking.model.Vehicle;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {

	@Query("select v from Vehicle v where plate = ?1")
	Vehicle findByPlate(String plate);

}
