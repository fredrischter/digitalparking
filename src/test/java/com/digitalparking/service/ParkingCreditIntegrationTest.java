package com.digitalparking.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.digitalparking.dto.StartParking;
import com.digitalparking.dto.StopParking;
import com.digitalparking.exception.CreditNotAvailableException;
import com.digitalparking.exception.CustomerNotFoundException;
import com.digitalparking.exception.InvalidStatusException;
import com.digitalparking.exception.ParkingLotNotFoundException;
import com.digitalparking.exception.ParkingSessionFoundException;
import com.digitalparking.exception.VehicleNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingCreditIntegrationTest {

	private static final int MINUTES_BY_HOUR = 60;

	@Autowired
	ParkingService parkingService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	WalletService walletService;

	@Test(expected = CreditNotAvailableException.class)
	public void andrewHasntCreditBecauseHeNeverParkedBefore() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// When
		parkForMinutes("A111", MINUTES_BY_HOUR * 3);
	}

	@Test
	public void andrewHavingCreditBecauseParkedBefore() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("A111", 1);
		
		// When
		int balance = getBalance("andrew@test.com");
		parkForMinutes("A111", MINUTES_BY_HOUR * 3);
		balance = getBalance("andrew@test.com");
		
		// Then
		assertEquals(-4, balance);
	}

	@Test(expected = CreditNotAvailableException.class)
	public void andrewDoesntHaveEnoughCredit() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("A111", MINUTES_BY_HOUR * 10);
	}
	
	@Test(expected = CreditNotAvailableException.class)
	public void andrewDidntHaveBalanceForGettingCredit() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		walletService.withdraw(1, 6);
		
		// When
		parkForMinutes("A111", MINUTES_BY_HOUR * 11);
	}
	
	private int getBalance(String email) throws CustomerNotFoundException {
		return customerService.getBalance(email);
	}

	private void parkForMinutes(String plate, int minutes) throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		parkingService.startParking(1, StartParking.builder().licensePlateNumber(plate).build(), LocalDateTime.of(2018, 1,  1, 10, 0, 0));
		parkingService.endParking(1, plate, StopParking.builder().status(ParkingService.STOPPED).build(), LocalDateTime.of(2018, 1,  1, 10 + minutes / MINUTES_BY_HOUR, minutes % MINUTES_BY_HOUR, 0));
	}
	
}
