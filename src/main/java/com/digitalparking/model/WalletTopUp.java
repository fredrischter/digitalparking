package com.digitalparking.model;

import javax.persistence.Entity;
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
public class WalletTopUp {

	@NotNull
	@Id
	Integer id;

	@NotNull
	Integer customerId;
	
	@NotNull
	Integer value;
	
	String description;
	
}
