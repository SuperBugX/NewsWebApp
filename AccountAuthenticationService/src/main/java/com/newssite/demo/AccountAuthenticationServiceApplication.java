package com.newssite.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.newssite.demo.repositories.AccountRepository;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = AccountRepository.class)
public class AccountAuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountAuthenticationServiceApplication.class, args);
	}
}
