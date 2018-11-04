package com.digitalparking.service;

import org.springframework.stereotype.Service;

import com.digitalparking.model.Customer;
import com.digitalparking.model.ParkingSession;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	public void sendEmail(ParkingSession parkingSession, Customer customer) {
		log.info("Email has been sent to "+customer.getEmail()+" "+parkingSession);
	}
}
