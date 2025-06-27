package com.ajaysarwade.Treading.model;

import com.ajaysarwade.Treading.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fullName;
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private String mobile;
	
	@Embedded
	private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
	
	private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
	

}
