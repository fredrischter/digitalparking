package com.digitalparking;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.digitalparking.dto.StartParking;
import com.digitalparking.dto.StopParking;
import com.digitalparking.exception.CreditNotAvailableException;
import com.digitalparking.exception.InvalidStatusException;
import com.digitalparking.exception.ParkingLotNotFoundException;
import com.digitalparking.exception.ParkingSessionFoundException;
import com.digitalparking.exception.UserNotFoundException;
import com.digitalparking.exception.VehicleNotFoundException;
import com.digitalparking.service.CustomerService;
import com.digitalparking.service.ParkingService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerIntegrationTest {

	private static final int MINUTES_BY_HOUR = 60;

	@Autowired
	ParkingService parkingService;
	
	@Autowired
	CustomerService customerService;

	@Test
	public void andrewParkedBy2Hours() throws UserNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("A111", 120);
		
		// When
		int balance = getBalance("andrew@test.com");
		
		// Then
		assertEquals(balance, 1);
	}

	@Test
	public void maryBy1hourAndHalf() throws UserNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("M111", 90);
		
		// When
		int balance = getBalance("mary@test.com");
		
		// Then
		assertEquals(balance, 13);
	}


	private int getBalance(String email) {
		return customerService.getBalance(email);
	}

	private void parkForMinutes(String plate, int minutes) throws UserNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		parkingService.startParking(1, StartParking.builder().licensePlateNumber(plate).build(), LocalDateTime.of(2018, 1,  1, 10, 0, 0));
		parkingService.endParking(1, plate, StopParking.builder().status(ParkingService.STOPPED).build(), LocalDateTime.of(2018, 1,  1, 10 + minutes / MINUTES_BY_HOUR, minutes % MINUTES_BY_HOUR, 0));
	}
	
}
