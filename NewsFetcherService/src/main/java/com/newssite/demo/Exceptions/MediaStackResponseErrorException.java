package com.newssite.demo.exceptions;

public class MediaStackResponseErrorException extends MediaStackException {
	
	public MediaStackResponseErrorException(String msg) {
		super(msg);
	}
	
	public MediaStackResponseErrorException() {
		super();
	}
}
