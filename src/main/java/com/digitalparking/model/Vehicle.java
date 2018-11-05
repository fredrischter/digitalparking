package com.digitalparking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotNull
	Integer customerId;
	
	@NotNull
	String plate;
	
	String description;
	
}
