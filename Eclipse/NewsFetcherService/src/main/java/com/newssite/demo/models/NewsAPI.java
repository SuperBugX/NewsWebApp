package com.newssite.demo.models;

import java.net.URI;

import java.time.LocalDate;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.interfaces.LatestNewsAPI;
import com.newssite.demo.interfaces.SearcherNewsAPI;
import com.newssite.demo.resources.Article;

import lombok.Builder;

@Builder
public class NewsAPI implements LatestNewsAPI, SearcherNewsAPI {

	// All possible API parameter values updated as of 06/04/21.

	// Attributes
	// Registered API key
	private final String APIKEY = "7783e4d68a23493a95b49f8594dbb506";
	// API Host URI
	private final String HOST = "https://newsapi.org";
	// End-Points
	private final String LIVENEWS = "/v2/top-headlines";
	private final String SEARCHNEWS = "/v2/everything";

	/*
	 * Supported Categories: general, business, entertainment, health, science,
	 * sports technology,
	 */
	private String[] includedCategories;

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

	private String[] includedCountries;
	/*
	 * Supported languages: ar - Arabic de - German en - English es - Spanish fr -
	 * French he - Hebrew it - Italian nl - Dutch no - Norwegian pt - Portuguese ru
	 * - Russian se - Swedish zh - Chinese
	 *
	 */
	private String[] includedLanguages;

	// Used to forcefully include or exclude keywords from any results
	private String[] includedKeyWords;
	private String[] excludedKeyWords;

	// Used to forcefully include or exclude news sources from any results
	// Maximum of 20 allowed for article searching
	// Example: bbc
	private String[] includedSources;
	private String[] excludedSources;

	// Forcefully include or exclude articles found from specific domains
	// Example: techcrunch.com

	private String[] includedDomains;
	private String[] excludedDomains;

	// Date filters for article searching
	private LocalDate from;
	private LocalDate until;

	// Maximum pagination limit is 100, Default is 100
	private int paginationLimit;

	// Pagination offset, Default is 1
	private int paginationOffset;

	// Supported sorting values: relevancy, popularity, publishedAt
	// Default: publishedAt
	private String sortBy;

	// Methods
	public String getLatestNews() throws NewsAPIResponseErrorException, NewsAPIJSONException {

		String apiJsonResponse = null;
		JsonNode responseNode = null;
		JsonNode dataNode = null;
		ObjectMapper jsonMapper = new ObjectMapper();

		// Build a request based on the available variables and get a JSON response
		try {

			apiJsonResponse = (WebClient.builder().build()).get()
					.uri(HOST, uriBuilder -> buildLiveArticlesRequest(uriBuilder)).retrieve().bodyToMono(String.class)
					.block();

			// Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("status");

			// Check if a error response was provided
			if (dataNode.toString().equals("error")) {
				throw new NewsAPIResponseErrorException("Received the error:" + dataNode.asText());
			}
		} catch (WebClientResponseException e) {
			throw new NewsAPIResponseErrorException("Unable to complete the request");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}

		// Return the JSON list of articles
		return apiJsonResponse;
	}

	public Article[] convertLatestNewsToArticles(String json) throws NewsAPIJSONException {

		JsonNode responseNode;
		JsonNode dataNode;
		JsonNode articleNode;
		Article tempArticle;
		ObjectMapper jsonMapper = new ObjectMapper();

		String category = "";
		String country = "";
		String language = "";

		try {
			responseNode = jsonMapper.readTree(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}

		if (includedCategories != null) {
			category = category.concat(String.join(",", includedCategories));
		}

		if (includedLanguages != null) {
			language = language.concat(String.join(",", includedLanguages));
		}

		if (includedCountries != null) {
			country = country.concat(String.join(",", includedCountries));
		}

		dataNode = responseNode.get("articles");
		Article[] articles = new Article[dataNode.size()];

		// Loop through all of the received JSON articles and convert them into a custom
		// article object
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

	private URI buildLiveArticlesRequest(UriBuilder uriBuilder) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(LIVENEWS).queryParam("apiKey", APIKEY);

		StringBuffer stringBuffer = new StringBuffer();

		// Set categories
		if (includedCategories != null) {
			stringBuffer.append(String.join(",", includedCategories));
		}

		uriBuilder.queryParam("category", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set countries
		if (includedCountries != null) {
			stringBuffer.append(String.join(",", includedCountries));

			uriBuilder.queryParam("country", stringBuffer.toString());

			stringBuffer.setLength(0);
		} else {

			// Set sources
			// Searching for the latest news does not work when using both specified
			// countries and sources
			if (includedSources != null) {
				stringBuffer.append(String.join(",", includedSources));
			}

			if (excludedSources != null) {

				if (stringBuffer.length() > 0) {
					stringBuffer.append(",");
				}
				stringBuffer.append(String.join(",", excludedSources));
			}

			uriBuilder.queryParam("sources", stringBuffer.toString());

			stringBuffer.setLength(0);
		}

		// Set keywords
		if (includedKeyWords != null) {
			stringBuffer.append(String.join(",", includedKeyWords));
		}

		if (excludedKeyWords != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",", excludedKeyWords));
		}

		uriBuilder.queryParam("q", stringBuffer.toString());

		stringBuffer.setLength(0);

		if (paginationLimit != 0) {
			uriBuilder.queryParam("pageSize", paginationLimit);
		}

		if (paginationOffset != 0) {
			uriBuilder.queryParam("page", paginationOffset);
		}

		return uriBuilder.build();
	}

	@Override
	public String getSpecificNews() throws NewsAPIResponseErrorException, NewsAPIJSONException {

		String apiJsonResponse = null;
		JsonNode responseNode = null;
		JsonNode dataNode = null;
		ObjectMapper jsonMapper = new ObjectMapper();

		// Build a request based on the available variables and get a JSON response
		try {

			apiJsonResponse = (WebClient.builder().build()).get()
					.uri(HOST, uriBuilder -> buildSpecificArticlesRequest(uriBuilder)).retrieve()
					.bodyToMono(String.class).block();

			// Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("status");

			// Check if a error response was provided
			if (dataNode.toString().equals("error")) {
				throw new NewsAPIResponseErrorException("Received the error:" + dataNode.asText());
			}

		} catch (WebClientResponseException e) {
			throw new NewsAPIResponseErrorException("Unable to complete the request");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}

		// Return the JSON list of articles
		return apiJsonResponse;
	}

	@Override
	public Article[] convertSpecificNewsToArticles(String json) throws NewsAPIJSONException {
		JsonNode responseNode;
		JsonNode dataNode;
		JsonNode articleNode;
		Article tempArticle;
		ObjectMapper jsonMapper = new ObjectMapper();

		String category = "";
		String country = "";
		String language = "";

		try {
			responseNode = jsonMapper.readTree(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}

		if (includedCategories != null) {
			category = category.concat(String.join(",", includedCategories));
		}

		if (includedLanguages != null) {
			language = language.concat(String.join(",", includedLanguages));
		}

		if (includedCountries != null) {
			country = country.concat(String.join(",", includedCountries));
		}

		dataNode = responseNode.get("articles");
		Article[] articles = new Article[dataNode.size()];

		// Loop through all of the received JSON articles and convert them into a custom
		// article object
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

	private URI buildSpecificArticlesRequest(UriBuilder uriBuilder) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(SEARCHNEWS).queryParam("apiKey", APIKEY);

		StringBuffer stringBuffer = new StringBuffer();

		// Set keywords
		if (includedKeyWords != null) {
			stringBuffer.append(String.join(",", includedKeyWords));
		}

		if (excludedKeyWords != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedKeyWords));
		}

		uriBuilder.queryParam("q", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set sources
		if (includedSources != null) {
			stringBuffer.append(String.join(",", includedSources));
		}

		if (excludedSources != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedSources));
		}

		uriBuilder.queryParam("sources", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set domains
		if (includedDomains != null) {
			stringBuffer.append(String.join(",", includedDomains));
		}

		uriBuilder.queryParam("domains", stringBuffer.toString());

		stringBuffer.setLength(0);

		if (excludedDomains != null) {

			stringBuffer.append(String.join(",", excludedDomains));
		}

		uriBuilder.queryParam("excludeDomains", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set dates
		if (from != null) {
			uriBuilder.queryParam("from", from.toString());
		}

		if (until != null) {
			uriBuilder.queryParam("to", until.toString());
		}

		// Set languages
		if (includedLanguages != null) {
			stringBuffer.append(String.join(",", includedLanguages));
		}

		uriBuilder.queryParam("language", stringBuffer.toString());

		stringBuffer.setLength(0);

		if (paginationLimit != 0) {
			uriBuilder.queryParam("pageSize", paginationLimit);
		}

		if (paginationOffset != 0) {
			uriBuilder.queryParam("page", paginationOffset);
		}

		if (sortBy != null) {

			switch (sortBy) {
			case "popularity":
				sortBy = "popularity";
				break;

			case "latest":
				sortBy = "publishedAt";
				break;

			default:
				sortBy = "relevancy";
				break;
			}

			uriBuilder.queryParam("sortBy", sortBy);
		} else {
			uriBuilder.queryParam("sortBy", "relevancy");
		}

		return uriBuilder.build();
	}
}
