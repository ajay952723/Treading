package com.ajaysarwade.Treading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity

public class AssetModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private double quantity;
	
	private double buyPrice;
	
	@ManyToOne
	private CoinMarketData coin;
	
	@ManyToOne
	private User user;
	

}
