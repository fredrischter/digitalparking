package com.digitalparking.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class StartParking {

	@NotNull
	String licensePlateNumber;
	
}
