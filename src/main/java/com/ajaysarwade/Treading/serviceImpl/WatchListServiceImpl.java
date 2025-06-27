package com.ajaysarwade.Treading.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Watchlist;
import com.ajaysarwade.Treading.repository.WatchListRepository;
import com.ajaysarwade.Treading.service.WatchListService;

import jakarta.transaction.Transactional;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchListRepository watchListRespository;

    @Override
    public Watchlist createWathcList(User user) {
        Watchlist watchList = new Watchlist();
        watchList.setUser(user);
        return watchListRespository.save(watchList);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> watchList = watchListRespository.findById(id);
        if (watchList.isEmpty()) {
            throw new Exception("Watch List not found");
        }
        return watchList.get();
    }

    @Override
    public CoinMarketData addItemToWatchlist(CoinMarketData coin, User user) throws Exception {
        Watchlist watchlist = findUserWatchList(user.getId());
        if (watchlist.getCoins().contains(coin)) {
            watchlist.getCoins().remove(coin);
        } else {
            watchlist.getCoins().add(coin);
        }
        watchListRespository.save(watchlist);
        return coin;
    }

    @Override
    public Watchlist findUserWatchList(Long userId) throws Exception {
        return watchListRespository.findByUserId(userId)
                .orElseThrow(() -> new Exception("Watchlist Not Found"));
    }

    @Override
    @Transactional
    public void removeItemFromWatchlist(String coinId, User user) throws Exception {
        Watchlist watchlist = watchListRespository.findByUserId(user.getId())
                .orElseThrow(() -> new Exception("Watchlist not found"));

        List<CoinMarketData> updatedCoins = watchlist.getCoins().stream()
                .filter(coin -> !coin.getId().equals(coinId))
                .collect(Collectors.toList());

        watchlist.setCoins(updatedCoins);
        watchListRespository.save(watchlist);
    }
}
