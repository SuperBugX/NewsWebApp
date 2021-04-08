package com.example.demo.handlers;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import com.example.demo.resources.StompPrincipal;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		// generate user name by UUID
		return new StompPrincipal(UUID.randomUUID().toString());
	}
}
