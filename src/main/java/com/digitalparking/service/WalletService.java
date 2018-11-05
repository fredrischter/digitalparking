package com.digitalparking.service;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalparking.config.DigitalParkingConfiguration;
import com.digitalparking.exception.CreditNotAvailableException;
import com.digitalparking.model.ParkingSession;
import com.digitalparking.model.Transaction;
import com.digitalparking.repository.CustomerRepository;
import com.digitalparking.repository.ParkingLotRepository;
import com.digitalparking.repository.ParkingSessionRepository;
import com.digitalparking.repository.TransactionRepository;
import com.digitalparking.repository.VehicleRepository;

@Service
public class WalletService {

	private static final String CANNOT_HAVE_MORE_CREDIT_THAN = "Cannot have more credit than ";

	private static final String DON_T_HAVE_NEEDED_POSITIVE_AMOUNT_FOR_GETTING_CREDIT = "Don't have needed positive amount for getting credit.";

	private static final String HAVE_NEVER_PARKED_BEFORE = "Have never parked before.";

	private static final long TIMEMILIS_15_MIN = 1000l * 60l * 15l;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	ParkingSessionRepository parkingSessionRepository;

	@Autowired
	ParkingLotRepository parkingLotRepository;

	@Autowired
	DigitalParkingConfiguration digitalParkingConfiguration;

	@Autowired
	CustomerService customerService;

	@Autowired
	EmailService emailService;
	
	@Autowired
	ParkingService parkingService;
	
	public int calculateAmount(ParkingSession parkingSession) {

		long passedTime = parkingSession.getEnd().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
				- parkingSession.getStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		int slots = (int) Math.floor((double) passedTime / TIMEMILIS_15_MIN);
		return digitalParkingConfiguration.getAmountChergedBy15Min() * (1 + slots);
	}

	public void withdraw(Integer customerId, int value) throws CreditNotAvailableException {

		Integer balance = customerService.getBalance(customerId);

		if (balance < value) {
			verifyCreditAvailability(customerId, balance, value);
		}

		Transaction transaction = Transaction.builder().customerId(customerId).value(-value).build();

		transactionRepository.save(transaction);
	}

	public void verifyCreditAvailability(Integer customerId, Integer balance, Integer value)
			throws CreditNotAvailableException {
		if (!parkingService.hasAlreadyParkedBefore(customerId)) {
			throw new CreditNotAvailableException(HAVE_NEVER_PARKED_BEFORE);
		}

		if (balance < digitalParkingConfiguration.getPositiveAmountNeededForCredit()) {
			throw new CreditNotAvailableException(DON_T_HAVE_NEEDED_POSITIVE_AMOUNT_FOR_GETTING_CREDIT);
		}

		if (balance - value < - digitalParkingConfiguration.getCreditLimit()) {
			throw new CreditNotAvailableException(
					CANNOT_HAVE_MORE_CREDIT_THAN + digitalParkingConfiguration.getCreditLimit());
		}
	}

}
