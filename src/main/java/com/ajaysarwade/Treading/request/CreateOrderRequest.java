package com.ajaysarwade.Treading.request;

import com.ajaysarwade.Treading.domain.OrderType;

import lombok.Data;

@Data
public class CreateOrderRequest {
	
	private String coinId;
	private double quantity;
	private OrderType orderType;

}
