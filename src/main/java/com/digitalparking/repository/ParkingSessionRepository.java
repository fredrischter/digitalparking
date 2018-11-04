package com.digitalparking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.digitalparking.model.ParkingSession;

public interface ParkingSessionRepository extends CrudRepository<ParkingSession, Integer> {

	@Query("select count(1) from ParkingSession p where p.customerId = ?1 and p.end is not null")
	Integer countByCustomerIdAndEnded(Integer customerId);

	@Query("select p from ParkingSession p where p.vehicleId = ?1 and p.end is not null")
	ParkingSession findByVehicleIdAndNotEnded(Integer id);

}
