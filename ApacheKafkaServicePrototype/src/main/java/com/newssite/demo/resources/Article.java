package com.newssite.demo.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
