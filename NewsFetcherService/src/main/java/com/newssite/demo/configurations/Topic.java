package com.newssite.demo.configurations;

public enum Topic {
	
	GENERAL("general"),
	BUSINESS("business"),
	SCIENCE("science"),
	ENTERTAINMENT("entertainment"),
	HEALTH("health"),
	SPORTS("sports"),
	TECHNOLOGY("technology");
	
    private String type;

    Topic(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
