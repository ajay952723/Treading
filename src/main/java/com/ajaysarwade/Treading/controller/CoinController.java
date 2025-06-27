package com.ajaysarwade.Treading.controller;

import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.service.CoinMarketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    @Autowired
    private CoinMarketService coinMarketService;

    // 1. Get paginated list of coins
    @GetMapping("/list")
    public ResponseEntity<List<CoinMarketData>> getCoinList(@RequestParam(defaultValue = "1") int page) {
    	List<CoinMarketData> coins = coinMarketService.getCoinList(page);
    	return ResponseEntity.ok(coins);
    }

    // 2. Get market chart data for a coin
    @GetMapping("/{coinId}/market-chart")
    public ResponseEntity<String> getMarketChart(
            @PathVariable String coinId,
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(coinMarketService.getMarketChart(coinId, days));
    }

    // 3. Get detailed info about a coin
    @GetMapping("/{coinId}/details")
    public ResponseEntity<String> getCoinDetails(@PathVariable String coinId) {
        return ResponseEntity.ok(coinMarketService.getCoinsDetails(coinId));
    }

    // 4. Get coin data from DB by ID
    @GetMapping("/{coinId}")
    public ResponseEntity<CoinMarketData> findById(@PathVariable String coinId) throws Exception {
        return ResponseEntity.ok(coinMarketService.findById(coinId));
    }

    // 5. Search for a coin
    @GetMapping("/search")
    public ResponseEntity<String> searchCoin(@RequestParam String keyword) {
        return ResponseEntity.ok(coinMarketService.searchCoin(keyword));
    }

    // 6. Get top 50 coins by market cap
    @GetMapping("/top-50")
    public ResponseEntity<String> getTop50Coins() {
        return ResponseEntity.ok(coinMarketService.getTop50CoinsByMarketCapRank());
    }

    // 7. Get trending coins
    @GetMapping("/trending")
    public ResponseEntity<String> getTrendingCoins() {
        return ResponseEntity.ok(coinMarketService.getTrendingCoins());
    }
}
