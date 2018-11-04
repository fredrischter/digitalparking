package com.digitalparking.config;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class DigitalParkingConfiguration {

	Integer creditLimit;
	
	Integer positiveAmountNeededForCredit;
	
	Integer amountChergedBy15Min;
/*
    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName("localhost");
        connectionFactory.setPort(6379);
        return connectionFactory;
    }
*/
}
