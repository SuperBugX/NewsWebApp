package com.newssite.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.EurekaClient;

@RestController
@RequestMapping("/ClientDiscovery")
public class GatewayController {
	
	@Autowired
    @Lazy
    private EurekaClient eurekaClient;
	
	@RequestMapping("/NewsFetcherService")
	public int getInstance() {
		return eurekaClient.getApplication("News-Fetcher-Service").getInstances().get(0).getPort();		
	}

}
