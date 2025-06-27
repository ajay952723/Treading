package com.ajaysarwade.Treading.service;

import java.util.List;

import com.ajaysarwade.Treading.domain.OrderType;
import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.Order;
import com.ajaysarwade.Treading.model.OrderItem;
import com.ajaysarwade.Treading.model.User;

public interface OrderService {

	Order createOrder(User user, OrderItem orderItem, OrderType orderType);

	Order getOrderById(Long orderId) throws Exception;

	List<Order> getAllOrderOfUser(User userId, OrderType OrderType, String assetSymbol);

	Order processOrder(CoinMarketData coin, double quantity, OrderType orderType, User user) throws Exception;

}
