package com.digitalparking.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalparking.config.DigitalParkingConfiguration;
import com.digitalparking.dto.StartParking;
import com.digitalparking.dto.StopParking;
import com.digitalparking.exception.CreditNotAvailableException;
import com.digitalparking.exception.CustomerNotFoundException;
import com.digitalparking.exception.InvalidStatusException;
import com.digitalparking.exception.ParkingLotNotFoundException;
import com.digitalparking.exception.ParkingSessionFoundException;
import com.digitalparking.exception.VehicleNotFoundException;
import com.digitalparking.model.Customer;
import com.digitalparking.model.ParkingLot;
import com.digitalparking.model.ParkingSession;
import com.digitalparking.model.Vehicle;
import com.digitalparking.repository.CustomerRepository;
import com.digitalparking.repository.ParkingLotRepository;
import com.digitalparking.repository.ParkingSessionRepository;
import com.digitalparking.repository.TransactionRepository;
import com.digitalparking.repository.VehicleRepository;

@Service
public class ParkingService {

	public static final String STOPPED = "stopped";

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
	WalletService walletService;
	
	public void createAsset(ParkingLot parkingLot) {
		parkingLotRepository.save(parkingLot);
	}

	public void startParking(Integer assetId, StartParking plate)
			throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException {
		startParking(assetId, plate, LocalDateTime.now());
	}
	
	public void startParking(Integer assetId, StartParking plate, LocalDateTime startTime)
			throws CustomerNotFoundException, VehicleNotFoundException, ParkingLotNotFoundException {
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
			throw new CustomerNotFoundException();
		}

		ParkingSession parkingSession = ParkingSession.builder().customerId(vehicle.getCustomerId())
				.vehicleId(vehicle.getId()).start(startTime).build();

		parkingSessionRepository.save(parkingSession);

	}

	public void endParking(Integer assetId, String licencePlateNumber, @Valid StopParking stopParking)
			throws VehicleNotFoundException, CustomerNotFoundException, ParkingSessionFoundException,
			ParkingLotNotFoundException, CreditNotAvailableException, InvalidStatusException {
		endParking(assetId, licencePlateNumber, stopParking, LocalDateTime.now());
	}

	public void endParking(Integer assetId, String licencePlateNumber, @Valid StopParking stopParking, LocalDateTime endTime)
				throws VehicleNotFoundException, CustomerNotFoundException, ParkingSessionFoundException,
				ParkingLotNotFoundException, CreditNotAvailableException, InvalidStatusException {
		
		if (stopParking == null || !stopParking.getStatus().equals(STOPPED)) {
			throw new InvalidStatusException();
		}
		
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
			throw new CustomerNotFoundException();
		}

		ParkingSession parkingSession = parkingSessionRepository.findByVehicleIdAndNotEnded(vehicle.getId());

		if (parkingSession == null) {
			throw new ParkingSessionFoundException();
		}

		parkingSession.setEnd(endTime);

		int amountCharged = walletService.calculateAmount(parkingSession);
		parkingSession.setAmountCharged(amountCharged);

		walletService.withdraw(customer.get().getId(), amountCharged);

		parkingSessionRepository.save(parkingSession);

		emailService.sendEmail(parkingSession);

	}

	public boolean hasAlreadyParkedBefore(Integer customerId) {
		return parkingSessionRepository.countByCustomerIdAndEnded(customerId) > 0;
	}

}
