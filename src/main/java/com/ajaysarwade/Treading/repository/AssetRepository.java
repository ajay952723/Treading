package com.ajaysarwade.Treading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.AssetModel;

public interface AssetRepository extends JpaRepository<AssetModel, Long> {

    List<AssetModel> findByUserId(Long userId);

    AssetModel findByUserIdAndCoinId(Long userId, String coinId);
}
