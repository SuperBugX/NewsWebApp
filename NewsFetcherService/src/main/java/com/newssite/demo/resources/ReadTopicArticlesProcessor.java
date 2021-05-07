package com.newssite.demo.resources;

import java.util.LinkedList;
import java.util.List;

import com.newssite.demo.interfaces.TopicProcessor;

public class ReadTopicArticlesProcessor implements TopicProcessor {

	// Attributes
	private List<Article> topicArticles;

	// Constructor
	public ReadTopicArticlesProcessor() {
		super();
		topicArticles = new LinkedList<Article>();
	}

	//Getters
	public List<Article> getTopicArticles() {
		return topicArticles;
	}

	// Method
	@Override
	public void process(String key, Object message) {
		if (message instanceof Article) {
			topicArticles.add((Article) message);
		}
	}
}
