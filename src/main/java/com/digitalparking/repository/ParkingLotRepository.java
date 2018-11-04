package com.digitalparking.repository;

import org.springframework.data.repository.CrudRepository;

import com.digitalparking.model.ParkingLot;

public interface ParkingLotRepository extends CrudRepository<ParkingLot, Integer> {

}
