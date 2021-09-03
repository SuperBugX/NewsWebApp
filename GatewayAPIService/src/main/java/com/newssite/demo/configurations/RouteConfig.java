package com.newssite.demo.configurations;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		// Set the accepted routes which redirect all received messages to a desired
		// microservice instance using eureka
		return builder.routes().route(r -> r.path("/NewsFetcherService/**").uri("lb://News-Fetcher-Service/"))
				.route(r -> r.path("/RegistrationService/**").filters(f -> f.stripPrefix(1))
						.uri("lb://Registration-Service/"))
				.route(r -> r.path("/TopicSubscriptionService/**").filters(f -> f.stripPrefix(1))
						.uri("lb:ws://Topic-Subscription-Service"))
				.route(r -> r.path("/AccountAuthenticationService/**").filters(f -> f.stripPrefix(1))
						.uri("lb:ws://Account-Authentication-Service"))
				.build();
	}
}
