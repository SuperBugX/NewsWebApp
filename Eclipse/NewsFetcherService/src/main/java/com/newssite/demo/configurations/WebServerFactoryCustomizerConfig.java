package com.newssite.demo.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;

@Configuration
public class WebServerFactoryCustomizerConfig
		implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	//Attributes
	@Value("${port.number.min}")
	private Integer minPort;

	@Value("${port.number.max}")
	private Integer maxPort;

	//Methods
	@Override
	// This method is responsible for randomising the used port number (from a set
	// of available ports) of the microservice when deployed
	public void customize(ConfigurableServletWebServerFactory factory) {
		int port = SocketUtils.findAvailableTcpPort(minPort, maxPort);
		factory.setPort(port);
		System.getProperties().put("server.port", port);
	}
}