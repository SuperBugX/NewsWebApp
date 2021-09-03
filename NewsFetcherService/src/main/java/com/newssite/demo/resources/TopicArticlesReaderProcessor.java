package com.newssite.demo.resources;

import java.util.LinkedList;
import java.util.List;

import com.newssite.demo.interfaces.TopicProcessor;

public class TopicArticlesReaderProcessor implements TopicProcessor {

	// Attributes
	private List<Article> topicArticles;

	// Constructor
	public TopicArticlesReaderProcessor() {
		super();
		topicArticles = new LinkedList<Article>();
	}

	// Getters
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((topicArticles == null) ? 0 : topicArticles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopicArticlesReaderProcessor other = (TopicArticlesReaderProcessor) obj;
		if (topicArticles == null) {
			if (other.topicArticles != null)
				return false;
		} else if (!topicArticles.equals(other.topicArticles))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicArticlesReaderProcessor [topicArticles=" + topicArticles + "]";
	}
}
