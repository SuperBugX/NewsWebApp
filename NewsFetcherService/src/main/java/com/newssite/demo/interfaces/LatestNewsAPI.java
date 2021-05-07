package com.newssite.demo.interfaces;

import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.resources.Article;

public interface LatestNewsAPI {
	
	public String getLatestNews() throws NewsAPIResponseErrorException, NewsAPIJSONException;
	public Article[] convertLatestNewsToArticles(String json) throws NewsAPIJSONException;
	
}
