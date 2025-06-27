package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User  findByEmail(String email);

}
