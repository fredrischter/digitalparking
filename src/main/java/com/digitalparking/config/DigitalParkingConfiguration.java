package com.digitalparking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class DigitalParkingConfiguration {

	@Value("${app.creditLimit}")
	Integer creditLimit;

	@Value("${app.positiveAmountNeededForCredit}")
	Integer positiveAmountNeededForCredit;

	@Value("${app.amountChergedBy15Min}")
	Integer amountChergedBy15Min;
	
}
