package com.newssite.demo.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
//Class used for creating JSON defined error messages
public class ErrorTemplate {
	
	//Attributes
	@NonNull private String code;
	private String message;
	
	public String toJSON() {
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ObjectNode errorNode = mapper.createObjectNode();
		
		errorNode.put("code", code);
		errorNode.put("message", message);
		rootNode.set("error", errorNode);
		
		return rootNode.toString();
	}
}
