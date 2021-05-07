package com.newssite.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newssite.demo.models.User;
import com.newssite.demo.repositories.UserRepository;

@RestController
@RequestMapping("/RegistrationService")
public class RegistrationController {
	
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/RegisterAccount")
	public boolean registerAccount() {
		System.out.println("I GOT SOMEORUHO");
		userRepo.save(new User());
		return false;
	}
}
