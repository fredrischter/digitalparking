package com.digitalparking.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@NotNull
	@Id
	Integer id;
	
	@NotNull
	@Email
	String email;
	
	@NotNull
	@Size(min = 6, max = 16)
	String password;
	
}
