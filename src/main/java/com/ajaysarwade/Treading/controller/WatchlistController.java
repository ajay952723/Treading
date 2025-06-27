package com.ajaysarwade.Treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Watchlist;
import com.ajaysarwade.Treading.service.CoinMarketService;
import com.ajaysarwade.Treading.service.UserService;
import com.ajaysarwade.Treading.service.WatchListService;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinMarketService coinMarketService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getuserWatchList(
            @RequestHeader("Authorization") String jwt) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        System.out.println("Authenticated user email: " + email);

        User user = userService.findUserByEmail(email);
        Watchlist list = watchListService.findUserWatchList(user.getId());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getuserWatchList(
            @PathVariable Long watchlistId) throws Exception {

        Watchlist list = watchListService.findById(watchlistId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/add/coin/{coinId}")
    public ResponseEntity<CoinMarketData> addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        System.out.println("Authenticated user email: " + email);

        User user = userService.findUserByEmail(email);
        CoinMarketData coin = coinMarketService.findById(coinId);

        CoinMarketData addedCoin = watchListService.addItemToWatchlist(coin, user);
        return new ResponseEntity<>(addedCoin, HttpStatus.OK);
    }
    
    @DeleteMapping("/remove/coin/{coinId}")
    public ResponseEntity<?> removeItemFromWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userService.findUserByEmail(email);
        watchListService.removeItemFromWatchlist(coinId, user);

        return new ResponseEntity<>("Coin removed from watchlist", HttpStatus.OK);
    }

}
