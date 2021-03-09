package com.newssite.demo.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.newssite.demo.producers.Producer;

@RestController
@RequestMapping("/kafka")
public class ApacheController {
	
    private final Producer producerService;

    public ApacheController(Producer producerService) {
        this.producerService = producerService;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam String message) {
        producerService.sendMessage(message);
    }
	
	
}
