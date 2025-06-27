package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
