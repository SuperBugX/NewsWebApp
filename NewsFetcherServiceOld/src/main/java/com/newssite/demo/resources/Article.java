package com.newssite.demo.resources;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article {
	
	//Attributes
	private String author;
	private String title;
	private String description;
	private String url;
	private String source;
	private String imageUrl;
	private String category;
	private String language;
	private String countryOrigin;
	private String publishedAt;
}
