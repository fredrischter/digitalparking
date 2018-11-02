package com.digitalparking.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class ParkingSession {

	@NotNull
	@Id
	Integer id;

	@NotNull
	Integer customerId;

	@NotNull
	Integer vehicleId;
	
	@NotNull
	LocalDateTime start;
	
	LocalDateTime end;

	Integer amountCharged;
	
	Boolean invoiceSent;
	
}
