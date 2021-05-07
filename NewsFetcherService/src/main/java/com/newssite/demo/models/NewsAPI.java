package com.newssite.demo.models;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.newssite.demo.abstarct.AbstractNewsAPI;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.interfaces.LatestNewsAPI;
import com.newssite.demo.resources.Article;

import lombok.Builder;

@Builder
public class NewsAPI extends AbstractNewsAPI implements LatestNewsAPI {

	// All possible API parameter values updated as of 06/04/21.

	// Attributes
	// Registered API key
	// 7783e4d68a23493a95b49f8594dbb506
	// ca4d921c12a049beb7a1395ba27ad62f
	private final String APIKEY = "7783e4d68a23493a95b49f8594dbb506";

	// API Host URI
	private final String HOST = "https://newsapi.org";
	// Live News endPoint
	private final String LIVENEWS = "/v2/top-headlines";

	/*
	 * Supported Categories: general, business, entertainment, health, science,
	 * sports technology,
	 */
	private String category;

	/*
	 * Supported countries: ar - Argentina at - Austria au - Australia be - Belgium
	 * br - Brazil bg - Bulgaria ca - Canada cn - China co - Colombia cz - Czech
	 * Republic eg - Egypt fr - France de - Germany gr - Greece hk - Hong Kong hu -
	 * Hungary in - India id - Indonesia ie - Ireland il - Israel it - Italy jp -
	 * Japan lv - Latvia lt - Lithuania my - Malaysia mx - Mexico ma - Morocco nl -
	 * Netherlands nz - New Zealand ng - Nigeria no - Norway ph - Philippines pl -
	 * Poland pt - Portugal ro - Romania sa - Saudi Arabia rs - Serbia sg -
	 * Singapore sk - Slovakia si - Slovenia za - South Africa kr - South Korea se -
	 * Sweden ch - Switzerland tw - Taiwan th - Thailand tr - Turkey ae - UAE ua -
	 * Ukraine gb - United Kingdom us - United States ve - Venuzuela
	 */

	private String country;
	/*
	 * Supported languages: ar - Arabic de - German en - English es - Spanish fr -
	 * French he - Hebrew it - Italian nl - Dutch no - Norwegian pt - Portuguese ru
	 * - Russian se - Swedish zh - Chinese
	 *
	 */
	private String language;

	// General keywords to be used
	private String[] keyWords;

	// Contains news sources (organisations), Maximum 20 allowed
	private String[] sources;

	// Maximum pagination limit is 100, Default is 100
	private int paginationLimit;

	// Pagination offset, Default is 1
	private int paginationOffset;

	@Autowired
	WebClient.Builder webClientBuilder;

	// Methods
	public String getLatestNews() throws NewsAPIResponseErrorException, NewsAPIJSONException {

		String apiJsonResponse = null;
		JsonNode responseNode = null;
		JsonNode dataNode = null;
		ObjectMapper jsonMapper = new ObjectMapper();

		// Build a request based on the available variables and get a JSON response
		apiJsonResponse = (WebClient.builder().build()).get()
				.uri(HOST, uriBuilder -> buildTopHeadlineRequest(uriBuilder)).retrieve().bodyToMono(String.class)
				.block();

		try {
			// Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("status");

			// Check if a error response was provided
			if (dataNode.toString().equals("error")) {
				throw new NewsAPIResponseErrorException("Received the error:" + dataNode.asText());
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}

		// Return the JSON list of articles
		return apiJsonResponse;
	}

	private URI buildTopHeadlineRequest(UriBuilder uriBuilder) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(LIVENEWS).queryParam("apiKey", APIKEY);

		if (country != null && !country.equals("")) {
			uriBuilder.queryParam("country", country);
		}

		if (language != null && !language.equals("")) {
			uriBuilder.queryParam("language", language);
		}

		if (category != null && !category.equals("")) {
			uriBuilder.queryParam("category", category);
		}

		if (keyWords != null && !keyWords.equals(new String[] {})) {
			uriBuilder.queryParam("q", commaSeperateArray(keyWords));
		}

		if (sources != null && !sources.equals(new String[] {})) {
			uriBuilder.queryParam("sources", commaSeperateArray(sources));
		}

		if (paginationLimit != 0) {
			uriBuilder.queryParam("pageSize", paginationLimit);
		}

		if (paginationOffset != 0) {
			uriBuilder.queryParam("page", paginationOffset);
		}

		return uriBuilder.build();
	}

	public Article[] convertLatestNewsToArticles(String json) throws NewsAPIJSONException {

		JsonNode responseNode;
		JsonNode dataNode;
		JsonNode articleNode;
		Article tempArticle;
		ObjectMapper jsonMapper = new ObjectMapper();

		try {
			responseNode = jsonMapper.readTree(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}

		dataNode = responseNode.get("articles");
		Article[] articles = new Article[dataNode.size()];

		for (int i = 0; i < dataNode.size(); i++) {

			articleNode = dataNode.get(i);
			tempArticle = Article.builder().author(articleNode.get("author").asText()).category(category)
					.title(articleNode.get("title").asText()).url(articleNode.get("url").asText())
					.description(articleNode.get("description").asText())
					.imageUrl(articleNode.get("urlToImage").asText()).language(language).countryOrigin(country)
					.publishedAt(articleNode.get("publishedAt").asText())
					.source(articleNode.get("source").get("name").asText()).build();
			articles[i] = tempArticle;
		}

		return articles;
	}
}
