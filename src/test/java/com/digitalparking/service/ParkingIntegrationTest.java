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
public class ParkingIntegrationTest {

	private static final int MINUTES_BY_HOUR = 60;

	@Autowired
	ParkingService parkingService;
	
	@Autowired
	CustomerService customerService;

	@Test
	public void andrewParkedBy2Hours() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("A111", 120);
		
		// When
		int balance = getBalance("andrew@test.com");
		
		// Then
		assertEquals(1, balance);
	}

	@Test
	public void andrewParkedBy45min() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("A111", 45);
		
		// When
		int balance = getBalance("andrew@test.com");
		
		// Then
		assertEquals(6, balance);
	}

	@Test
	public void maryBy1hourAndHalf() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("M111", 90);
		
		// When
		int balance = getBalance("mary@test.com");
		
		// Then
		assertEquals(13, balance);
	}

	@Test
	public void maryBy1hourAnd105min() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("M111", 105);
		
		// When
		int balance = getBalance("mary@test.com");
		
		// Then
		assertEquals(12, balance);
	}

	@Test
	public void natthewBy45min() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("N111", 45);
		
		// When
		int balance = getBalance("nathew@test.com");
		
		// Then
		assertEquals(26, balance);
	}

	@Test
	public void natthewBy1hour45min() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("N111", MINUTES_BY_HOUR + 45);
		
		// When
		int balance = getBalance("nathew@test.com");
		
		// Then
		assertEquals(22, balance);
	}

	@Test
	public void zoeTwo2hours15min() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("Z111", MINUTES_BY_HOUR * 2 + 15);
		
		// When
		int balance = getBalance("zoe@test.com");
		
		// Then
		assertEquals(30, balance);
	}

	@Test
	public void zoeHalfHour() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("Z111", MINUTES_BY_HOUR  / 2);
		
		// When
		int balance = getBalance("zoe@test.com");
		
		// Then
		assertEquals(37, balance);
	}

	@Test(expected = VehicleNotFoundException.class)
	public void plateNotFound() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("X111", 1);
	}

	@Test(expected = CustomerNotFoundException.class)
	public void customerNotFound() throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		// Given
		parkForMinutes("Z111", 111);
		
		// When
		getBalance("zzzzzzzzzzzzzzz@test.com");
	}

	private int getBalance(String email) throws CustomerNotFoundException {
		return customerService.getBalance(email);
	}

	private void parkForMinutes(String plate, int minutes) throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException, ParkingSessionFoundException, CreditNotAvailableException, InvalidStatusException {
		parkingService.startParking(1, StartParking.builder().licensePlateNumber(plate).build(), LocalDateTime.of(2018, 1,  1, 10, 0, 0));
		parkingService.endParking(1, plate, StopParking.builder().status(ParkingService.STOPPED).build(), LocalDateTime.of(2018, 1,  1, 10 + minutes / MINUTES_BY_HOUR, minutes % MINUTES_BY_HOUR, 0));
	}
	
}
