package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ajaysarwade.Treading.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // ✅ Correct naming for Spring Data JPA
    Wallet findByUserId(Long userId);
}