package com.digitalparking.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Vehicle {

	@NotNull
	@Id
	Integer id;

	@NotNull
	Integer customerId;
	
	@NotNull
	@Id
	String plate;
	
	String description;
	
}
