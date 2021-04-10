package com.newssite.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.newssite.demo.details.AccountDetails;
import com.newssite.demo.entities.Account;
import com.newssite.demo.repositories.AccountRepository;

@Service
public class AccountDetailsService implements UserDetailsService {

	@Autowired
	AccountRepository accountRepository;

	@Override
	public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> account = accountRepository.findByUsername(username);
		account.orElseThrow(() -> new UsernameNotFoundException("Not Found: " + username));
		return account.map(AccountDetails::new).get();
	}
}
