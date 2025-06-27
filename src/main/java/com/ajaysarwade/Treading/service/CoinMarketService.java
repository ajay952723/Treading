package com.ajaysarwade.Treading.service;

import java.util.List;

import com.ajaysarwade.Treading.model.CoinMarketData;

public interface CoinMarketService {
	
	List<CoinMarketData> getCoinList(int page);
	
	String getMarketChart(String coinId,int days);
	
	String getCoinsDetails(String coinId);
	
	CoinMarketData findById(String coinId) throws Exception;
	
	String searchCoin(String keyword);
	
	String getTop50CoinsByMarketCapRank();
	
	String getTrendingCoins();

}
