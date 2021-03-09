package com.newssite.demo.configurations;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class CorsConfig {

	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "POST")
						.allowedHeaders("Origin", "Accept", "Content-Type", "Authorization").allowCredentials(true)
						.allowedOrigins("*");
			}
		};
	}

}
