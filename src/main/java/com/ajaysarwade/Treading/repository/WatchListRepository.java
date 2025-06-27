package com.ajaysarwade.Treading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.Watchlist;

public interface WatchListRepository extends JpaRepository<Watchlist, Long>{
	
	Optional<Watchlist> findByUserId(Long userId);

}
