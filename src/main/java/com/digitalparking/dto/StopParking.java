package com.digitalparking.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class StopParking {

	@NotNull
	String status;
	
}
