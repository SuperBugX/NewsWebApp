package com.newssite.demo.exceptions;

public class NewsAPIResponseErrorException extends NewsAPIException {
	
	public NewsAPIResponseErrorException(String msg) {
		super(msg);
	}
	
	public NewsAPIResponseErrorException() {
		super();
	}
}
