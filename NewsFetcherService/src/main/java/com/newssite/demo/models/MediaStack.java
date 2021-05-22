package com.newssite.demo.models;

import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.exceptions.NewsAPIException;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.interfaces.LatestNewsAPI;
import com.newssite.demo.interfaces.SearcherNewsAPI;
import com.newssite.demo.resources.Article;

import lombok.Builder;

@Builder
public class MediaStack implements LatestNewsAPI, SearcherNewsAPI {

	// All possible API parameter values updated as of 26/01/21.

	// Attributes
	// Registered API key
	private final String APIKEY = "2fc31c34f76d68a481bacaf3b874a961";
	// API Host URI
	private final String HOST = "http://api.mediastack.com";
	// End-Points
	private final String LIVENEWS = "/v1/news";
	private final String SEARCHNEWS = "/v1/news";

	// Used to forcefully include or exclude news sources from any results
	private String[] includedSources;
	private String[] excludedSources;

	/*
	 * Supported Categories: general, business, entertainment, health, science,
	 * sports, technology,
	 */
	private String[] includedCategories;
	private String[] excludedCategories;

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
	private String[] excludedCountries;

	/*
	 * Supported languages: ar - Arabic de - German en - English es - Spanish fr -
	 * French he - Hebrew it - Italian nl - Dutch no - Norwegian pt - Portuguese ru
	 * - Russian se - Swedish zh - Chinese
	 */

	private String[] includedLanguages;
	private String[] excludedLanguages;

	// Used to forcefully include or exclude keywords from any results
	private String[] includedKeyWords;
	private String[] excludedKeyWords;

	// Maximum pagination limit is 100, Default is 25
	private int paginationLimit;
	// Pagination Offset, works only if the number of found articles is greater than
	// the set pagination limit
	private int paginationOffset;

	// Supported sorting values: published_desc, published_acs, popularity
	private String sortBy;

	// Methods
	public String getLatestNews() throws NewsAPIResponseErrorException, NewsAPIJSONException {

		String apiJsonResponse;
		JsonNode responseNode;
		JsonNode dataNode;
		ObjectMapper jsonMapper = new ObjectMapper();

		// Build a request based on the available variables and get a JSON response
		try {

			apiJsonResponse = (WebClient.builder().build()).get()
					.uri(HOST, uriBuilder -> buildLiveArticlesRequest(uriBuilder)).retrieve().bodyToMono(String.class)
					.block();

			// Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("error");

			// Check if a error response was provided
			if (dataNode != null) {
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

	private URI buildLiveArticlesRequest(UriBuilder uriBuilder) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(LIVENEWS).queryParam("access_key", APIKEY);
		StringBuffer stringBuffer = new StringBuffer();

		// Set countries
		if (includedCountries != null) {
			stringBuffer.append(String.join(",", includedCountries));
		}

		if (excludedCountries != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedCountries));
		}

		uriBuilder.queryParam("countries", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set languages
		if (includedLanguages != null) {
			stringBuffer.append(String.join(",", includedLanguages));
		}

		if (excludedLanguages != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedLanguages));
		}

		uriBuilder.queryParam("languages", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set categories
		if (includedCategories != null) {
			stringBuffer.append(String.join(",", includedCategories));
		}

		if (excludedCategories != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedCategories));
		}

		uriBuilder.queryParam("categories", stringBuffer.toString());

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

		uriBuilder.queryParam("keywords", stringBuffer.toString());

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
				sortBy = "published_desc";
				break;

			default:
				sortBy = "published_desc";
				break;
			}

			uriBuilder.queryParam("sort", sortBy);
		} else {
			uriBuilder.queryParam("sort", "published_desc");
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

		dataNode = responseNode.get("data");
		Article[] articles = new Article[dataNode.size()];

		// Loop through all of the received JSON articles and convert them into a custom
		// article object
		for (int i = 0; i < dataNode.size(); i++) {

			articleNode = dataNode.get(i);
			tempArticle = Article.builder().author(articleNode.get("author").asText())
					.category(articleNode.get("category").asText()).title(articleNode.get("title").asText())
					.url(articleNode.get("url").asText()).description(articleNode.get("description").asText())
					.source(articleNode.get("source").asText()).imageUrl(articleNode.get("image").asText())
					.language(articleNode.get("language").asText()).countryOrigin(articleNode.get("country").asText())
					.publishedAt(articleNode.get("published_at").asText()).build();
			articles[i] = tempArticle;

		}

		// Return all of the articles converted
		return articles;
	}

	@Override
	public String getSpecificNews() throws NewsAPIResponseErrorException, NewsAPIJSONException {

		String apiJsonResponse;
		JsonNode responseNode;
		JsonNode dataNode;
		ObjectMapper jsonMapper = new ObjectMapper();

		// Build a request based on the available variables and get a JSON response
		try {
			apiJsonResponse = (WebClient.builder().build()).get()
					.uri(HOST, uriBuilder -> buildSpecificArticlesRequest(uriBuilder)).retrieve()
					.bodyToMono(String.class).block();

			// Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("error");

			// Check if a error response was provided
			if (dataNode != null) {
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

	private URI buildSpecificArticlesRequest(UriBuilder uriBuilder) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(SEARCHNEWS).queryParam("access_key", APIKEY);
		StringBuffer stringBuffer = new StringBuffer();

		// Set countries
		if (includedCountries != null) {
			stringBuffer.append(String.join(",", includedCountries));
		}

		if (excludedCountries != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedCountries));
		}

		uriBuilder.queryParam("countries", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set languages
		if (includedLanguages != null) {
			stringBuffer.append(String.join(",", includedLanguages));
		}

		if (excludedLanguages != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedLanguages));
		}

		uriBuilder.queryParam("languages", stringBuffer.toString());

		stringBuffer.setLength(0);

		// Set categories
		if (includedCategories != null) {
			stringBuffer.append(String.join(",", includedCategories));
		}

		if (excludedCategories != null) {

			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append(String.join(",-", excludedCategories));
		}

		uriBuilder.queryParam("categories", stringBuffer.toString());

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

		uriBuilder.queryParam("keywords", stringBuffer.toString());

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
				sortBy = "published_desc";
				break;

			default:
				sortBy = "published_desc";
				break;
			}

			uriBuilder.queryParam("sort", sortBy);
		} else {
			uriBuilder.queryParam("sort", "published_desc");
		}

		return uriBuilder.build();
	}

	@Override
	public Article[] convertSpecificNewsToArticles(String json) throws NewsAPIJSONException {
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

		dataNode = responseNode.get("data");
		Article[] articles = new Article[dataNode.size()];

		// Loop through all of the received JSON articles and convert them into a custom
		// article object
		for (int i = 0; i < dataNode.size(); i++) {

			articleNode = dataNode.get(i);
			tempArticle = Article.builder().author(articleNode.get("author").asText())
					.category(articleNode.get("category").asText()).title(articleNode.get("title").asText())
					.url(articleNode.get("url").asText()).description(articleNode.get("description").asText())
					.source(articleNode.get("source").asText()).imageUrl(articleNode.get("image").asText())
					.language(articleNode.get("language").asText()).countryOrigin(articleNode.get("country").asText())
					.publishedAt(articleNode.get("published_at").asText()).build();
			articles[i] = tempArticle;

		}

		// Return all of the articles converted
		return articles;
	}
}
