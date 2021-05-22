package com.newssite.demo.interfaces;

import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.resources.Article;

public interface SearcherNewsAPI {
	
	public String getSpecificNews() throws NewsAPIResponseErrorException, NewsAPIJSONException;
	public Article[] convertSpecificNewsToArticles(String json) throws NewsAPIJSONException;	
}
