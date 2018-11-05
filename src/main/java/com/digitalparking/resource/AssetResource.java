package com.digitalparking.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalparking.dto.StartParking;
import com.digitalparking.dto.StopParking;
import com.digitalparking.exception.CreditNotAvailableException;
import com.digitalparking.exception.CustomerNotFoundException;
import com.digitalparking.exception.InvalidStatusException;
import com.digitalparking.exception.ParkingLotNotFoundException;
import com.digitalparking.exception.ParkingSessionFoundException;
import com.digitalparking.exception.VehicleNotFoundException;
import com.digitalparking.model.ParkingLot;
import com.digitalparking.service.ParkingService;

@RestController
@RequestMapping(path = "/pms/v1/assets")
public class AssetResource {
	
	@Autowired
	ParkingService parkingService; 

	@PostMapping("")
	public void addAsset(@Valid @RequestBody ParkingLot parkingLot) {
		parkingService.createAsset(parkingLot);
	}

	@PostMapping("{asset}/sessions")
	public void startParking(@PathVariable("asset") Integer assetId, @Valid @RequestBody StartParking startParking) throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException {
		parkingService.startParking(assetId, startParking);
	}
	
	@PostMapping("{asset}/vehicle/{licencePlateNumber}/session")
	public void endParking(@PathVariable("asset") Integer assetId, @PathVariable("licencePlateNumber") String licencePlateNumber, @Valid @RequestBody StopParking stopParking) throws VehicleNotFoundException, CustomerNotFoundException, ParkingSessionFoundException, ParkingLotNotFoundException, CreditNotAvailableException, InvalidStatusException {
		parkingService.endParking(assetId, licencePlateNumber, stopParking);
	}
}
