package com.digitalparking.resource;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalparking.dto.StartParking;
import com.digitalparking.dto.StopParking;
import com.digitalparking.exception.CreditNotAvailableException;
import com.digitalparking.exception.ParkingLotNotFoundException;
import com.digitalparking.exception.ParkingSessionFoundException;
import com.digitalparking.exception.UserNotFoundException;
import com.digitalparking.exception.VehicleNotFoundException;
import com.digitalparking.model.ParkingLot;
import com.digitalparking.service.ParkingService;

@RestController("/pms/v1/assets")
public class CustomerResource {
	
	@Autowired
	ParkingService parkingService; 

	@PostMapping("/")
	public void addAsset(@Valid ParkingLot parkingLot) throws UserNotFoundException, VehicleNotFoundException {
		parkingService.createAsset(parkingLot);
	}

	@PostMapping("/{asset}/sessions")
	public void startParking(@PathParam("asset") Integer assetId, @Valid StartParking startParking) throws UserNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException {
		parkingService.startParking(assetId, startParking);
	}
	
	@PostMapping("/{asset}/vehicle/$licencePlateNumber/session")
	public void endParking(@PathParam("asset") Integer assetId, @PathParam("licencePlateNumber") String licencePlateNumber, @Valid StopParking stopParking) throws VehicleNotFoundException, UserNotFoundException, ParkingSessionFoundException, ParkingLotNotFoundException, CreditNotAvailableException {
		parkingService.endParking(assetId, licencePlateNumber, stopParking);
	}
}
