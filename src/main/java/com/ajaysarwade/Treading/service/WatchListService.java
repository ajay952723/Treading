package com.ajaysarwade.Treading.service;


import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Watchlist;

public interface WatchListService {
	
	Watchlist findUserWatchList(Long userId) throws Exception ;
	Watchlist createWathcList(User user);
	Watchlist findById(Long id) throws Exception;
	
	CoinMarketData addItemToWatchlist(CoinMarketData coin,User user) throws Exception;
	
	void removeItemFromWatchlist(String coinId, User user) throws Exception;


}
