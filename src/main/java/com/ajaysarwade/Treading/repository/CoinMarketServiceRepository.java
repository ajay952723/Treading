package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.CoinMarketData;

public interface CoinMarketServiceRepository extends JpaRepository<CoinMarketData, String>{

}
