package com.digitalparking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.digitalparking.model.ParkingSession;
import com.digitalparking.repository.ParkingSessionRepository;

@Service
public class InvoiceService {
	
	@Autowired
	ParkingSessionRepository parkingSessionRepository;

	public void sendInvoice(ParkingSession parkingSession) {
		// TODO Auto-generated method stub
		
		//parkingSession.invoiceSent(true);
		//parkingSessionRepository.save(parkingSession);
		/*Send invoice with basic HTML containing:
			○ session start time
			○ stop time
			○ vehicle’s number
			○ total cost of the session that user’s wallet has been charged*/
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendNotSentInvoices() {
		// TODO Auto-generated method stub
	}
	
}
