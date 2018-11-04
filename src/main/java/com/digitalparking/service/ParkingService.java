package com.digitalparking.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalparking.config.DigitalParkingConfiguration;
import com.digitalparking.dto.StartParking;
import com.digitalparking.dto.StopParking;
import com.digitalparking.exception.CreditNotAvailableException;
import com.digitalparking.exception.ParkingLotNotFoundException;
import com.digitalparking.exception.ParkingSessionFoundException;
import com.digitalparking.exception.UserNotFoundException;
import com.digitalparking.exception.VehicleNotFoundException;
import com.digitalparking.model.Customer;
import com.digitalparking.model.ParkingLot;
import com.digitalparking.model.ParkingSession;
import com.digitalparking.model.Transaction;
import com.digitalparking.model.Vehicle;
import com.digitalparking.repository.CustomerRepository;
import com.digitalparking.repository.ParkingLotRepository;
import com.digitalparking.repository.ParkingSessionRepository;
import com.digitalparking.repository.TransactionRepository;
import com.digitalparking.repository.VehicleRepository;

@Service
public class ParkingService {

	private static final String CANNOT_HAVE_MORE_CREDIT_THAN = "Cannot have more credit than ";

	private static final String DON_T_HAVE_NEEDED_POSITIVE_AMOUNT_FOR_GETTING_CREDIT = "Don't have needed positive amount for getting credit.";

	private static final String HAVE_NEVER_PARKED_BEFORE = "Have never parked before.";

	private static final long TIMEMILIS_15_MIN = 1000 * 60 * 15;

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
	InvoiceService invoiceService;

	public void createAsset(ParkingLot parkingLot) throws UserNotFoundException, VehicleNotFoundException {
		parkingLotRepository.save(parkingLot);
	}
	
	public void startParking(Integer assetId, StartParking plate) throws UserNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException {
		Vehicle vehicle = vehicleRepository.findByPlate(plate.getLicensePlateNumber());

		if (vehicle == null) {
			throw new VehicleNotFoundException();
		}
		
		Optional<ParkingLot> parkingLot = parkingLotRepository.findById(assetId);
		
		if (!parkingLot.isPresent()) {
			throw new ParkingLotNotFoundException();
		}

		Optional<Customer> customer = customerRepository.findById(vehicle.getCustomerId());

		if (!customer.isPresent()) {
			throw new UserNotFoundException();
		}

		ParkingSession parkingSession = ParkingSession.builder()
				.customerId(vehicle.getCustomerId()).vehicleId(vehicle.getId())
				.start(LocalDateTime.now()).build();

		parkingSessionRepository.save(parkingSession);

	}

	public void endParking(Integer assetId, String licencePlateNumber, @Valid StopParking stopParking) throws VehicleNotFoundException, UserNotFoundException, ParkingSessionFoundException, ParkingLotNotFoundException, CreditNotAvailableException {
		Vehicle vehicle = vehicleRepository.findByPlate(licencePlateNumber);

		if (vehicle == null) {
			throw new VehicleNotFoundException();
		}
		
		Optional<ParkingLot> parkingLot = parkingLotRepository.findById(assetId);
		
		if (!parkingLot.isPresent()) {
			throw new ParkingLotNotFoundException();
		}

		Optional<Customer> customer = customerRepository.findById(vehicle.getCustomerId());

		if (!customer.isPresent()) {
			throw new UserNotFoundException();
		}
		
		ParkingSession parkingSession = parkingSessionRepository.findByVehicleIdAndNotEnded(vehicle.getId());

		if (parkingSession == null) {
			throw new ParkingSessionFoundException();
		}
		
		parkingSession.setEnd(LocalDateTime.now());
		
		int amountCharged = calculateAmount(parkingSession);
		parkingSession.setAmountCharged(amountCharged);
		
		withdraw(customer.get().getId(), amountCharged);
		
		parkingSessionRepository.save(parkingSession);
		
		invoiceService.sendInvoice(parkingSession);

	}

	private int calculateAmount(ParkingSession parkingSession) {
		
		long passedTime = parkingSession.getEnd().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - parkingSession.getStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();		
		int slots = (int)Math.floor(passedTime/TIMEMILIS_15_MIN);
		int amountCharged = digitalParkingConfiguration.getAmountChergedBy15Min() * (1 + slots);
		
		return amountCharged;
	}

	private void withdraw(Integer customerId, int value)
			throws CreditNotAvailableException {

		Integer balance = customerService.getBalance(customerId);

		if (balance < value) {
			verifyCreditAvailability(customerId, balance, value);
		}

		Transaction transaction = Transaction.builder().customerId(customerId)
				.value(-value).build();

		transactionRepository.save(transaction);
	}

	private void verifyCreditAvailability(Integer customerId, Integer balance,
			Integer value) throws CreditNotAvailableException {
		if (!hasAlreadyParkedBefore(customerId)) {
			throw new CreditNotAvailableException(HAVE_NEVER_PARKED_BEFORE);
		}

		if (balance < digitalParkingConfiguration
				.getPositiveAmountNeededForCredit()) {
			throw new CreditNotAvailableException(
					DON_T_HAVE_NEEDED_POSITIVE_AMOUNT_FOR_GETTING_CREDIT);
		}

		if (balance - value < digitalParkingConfiguration.getCreditLimit()) {
			throw new CreditNotAvailableException(CANNOT_HAVE_MORE_CREDIT_THAN
					+ digitalParkingConfiguration.getCreditLimit());
		}
	}

	public boolean hasAlreadyParkedBefore(Integer customerId) {
		return parkingSessionRepository.countByCustomerIdAndEnded(customerId) > 0;
	}

}
