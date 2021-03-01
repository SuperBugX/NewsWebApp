package com.newssite.demo.Exceptions;

public class MediaStackResponseErrorException extends MediaStackException {
	
	public MediaStackResponseErrorException(String msg) {
		super(msg);
	}
	
	public MediaStackResponseErrorException() {
		super();
	}
}
