package com.newssite.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newssite.demo.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
