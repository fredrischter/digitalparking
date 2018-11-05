package com.digitalparking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.digitalparking.model.Customer;
import com.digitalparking.model.ParkingSession;
import com.digitalparking.model.Vehicle;
import com.digitalparking.repository.CustomerRepository;
import com.digitalparking.repository.ParkingSessionRepository;
import com.digitalparking.repository.VehicleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
	
	private static final String SEPARATOR = " - ";

	private static final String COST = "Cost: ";

	private static final String VEHICLE2 = "Vehicle: ";

	private static final String PARKING_TIME = "Parking time: ";

	private static final String PART_CLOSE = "</div>";

	private static final String HTML_CLOSE = "</html>";

	private static final String PART = "<div>";

	private static final String HTML = "<html>";

	@Autowired
	VehicleRepository vehicleRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	ParkingSessionRepository parkingSessionRepository;

	public void sendEmail(ParkingSession parkingSession) {
		Customer customer = customerRepository.findById(parkingSession.getCustomerId()).get();
		String htmlEmail = buildHTMLEmail(parkingSession);
		
		log.info("Email has been sent to "+customer.getEmail()+": "+htmlEmail);
		
		parkingSession.setInvoiceSent(true);
		parkingSessionRepository.save(parkingSession);
	}

	private String buildHTMLEmail(ParkingSession parkingSession) {
		Vehicle vehicle = vehicleRepository.findById(parkingSession.getVehicleId()).get();
		
		StringBuilder sb = new StringBuilder();
		sb.append(HTML);
		sb.append(PART+PARKING_TIME+parkingSession.getStart()+SEPARATOR+parkingSession.getEnd()+PART_CLOSE);
		sb.append(PART+VEHICLE2+vehicle.getPlate()+PART_CLOSE);
		sb.append(PART+COST+parkingSession.getAmountCharged()+PART_CLOSE);
		sb.append(HTML_CLOSE);
		return sb.toString();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendNotSentInvoices() {
		List<ParkingSession> parkingSessions = parkingSessionRepository.findNotSent();
		
		parkingSessions.stream().forEach(this::sendEmail);
	}
}
