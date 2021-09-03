package com.newssite.demo.exceptions;

public class NewsAPIException extends Exception {
	
	public NewsAPIException(String msg) {
		super(msg);
	}
	
	public NewsAPIException() {
		super();
	}
}
