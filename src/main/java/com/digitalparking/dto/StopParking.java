package com.digitalparking.dto;

import javax.validation.constraints.NotNull;

import com.digitalparking.dto.StartParking.StartParkingBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StopParking {

	@NotNull
	String status;
	
}
