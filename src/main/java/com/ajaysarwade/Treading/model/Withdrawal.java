package com.ajaysarwade.Treading.model;

import java.time.LocalDateTime;

import com.ajaysarwade.Treading.domain.WithDrawalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Withdrawal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WithDrawalStatus status;
	
	
	private Long amount;
	
	@ManyToOne
	private User user;
	
	private LocalDateTime date= LocalDateTime.now();

}
