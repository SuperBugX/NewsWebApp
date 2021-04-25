package com.newssite.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
	
	@GetMapping("/user/preferences")
	public String getUserCategoryPreferences() {
		return "HELLO";
	}
}
