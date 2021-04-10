package com.newssite.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newssite.demo.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
	Optional<Account> findByUsername(String username);
}
