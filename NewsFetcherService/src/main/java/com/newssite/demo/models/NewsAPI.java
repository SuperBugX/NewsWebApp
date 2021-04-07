package com.newssite.demo.models;

import java.net.URI;
import java.util.Date;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.resources.Article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsAPI {

	// All possible API parameter values updated as of 06/04/21.

	// Attributes
	// Registered API key
	private final String APIKEY = "ca4d921c12a049beb7a1395ba27ad62f";

	// API Host URI
	private final String HOST = "https://newsapi.org";
	// Live News endPoint
	private final String LIVENEWS = "/v2/everything";

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

	// Keywords to be specifically matched with article titles
	private String[] titleKeyWords;

	// Contains news sources (organisations), Maximum 20 allowed
	private String[] sources;

	// Contains the website domains of news sources to narrow the article searching
	private String[] domains;

	// Contains a list of website domains of news sources to exclude when article
	// searching
	private String[] excludeDomains;

	// Supported sorting values: relevancy, popularity, publishedAt.
	private String sortBy;

	// Maximum pagination limit is 100, Default is 100
	private int paginationLimit;

	// Pagination offset, Default is 1
	private int paginationOffset;

	// A date attribute for the oldest article allowed. This should be in ISO 8601
	// format
	private Date fromDate;
	// A date attribute for the newest article allowed. This should be in ISO 8601
	// format
	private Date toDate;

	// Methods
	public String getLiveArticles() throws NewsAPIResponseErrorException, NewsAPIJSONException {

		String apiJsonResponse = null;
		JsonNode responseNode = null;
		JsonNode dataNode = null;
		ObjectMapper jsonMapper = new ObjectMapper();

		// Build a request based on the available variables and get a JSON response
		apiJsonResponse = (WebClient.builder().build()).get().uri(HOST, uriBuilder -> buildLiveNewsRequest(uriBuilder))
				.retrieve().bodyToMono(String.class).block();

		try {
			// Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("status");

			// Check if a error response was provided
			if (dataNode.toString().equals("error")) {
				throw new NewsAPIResponseErrorException("Received the error:" + dataNode.asText());
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}

		// Return the JSON list of articles
		return apiJsonResponse;
	}

	public URI buildLiveNewsRequest(UriBuilder uriBuilder) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(LIVENEWS).queryParam("apiKey", APIKEY);

		uriBuilder.queryParam("country", country);

		uriBuilder.queryParam("language", language);

		uriBuilder.queryParam("category", category);

		uriBuilder.queryParam("q", commaSeperateArray(keyWords));

		uriBuilder.queryParam("sources", commaSeperateArray(sources));

		uriBuilder.queryParam("pageSize", paginationLimit);

		uriBuilder.queryParam("page", paginationOffset);

		if (sortBy != null) {
			uriBuilder.queryParam("sort", sortBy);
		} else {
			uriBuilder.queryParam("sort", "publishedAt");
		}

		System.out.println("I SENT" + uriBuilder.build().toString());
		return uriBuilder.build();
	}

	public Article[] convertJSONToArticles(String json) throws JsonMappingException, JsonProcessingException {

		JsonNode responseNode;
		JsonNode dataNode;
		JsonNode articleNode;
		Article tempArticle;
		ObjectMapper jsonMapper = new ObjectMapper();
		
		responseNode = jsonMapper.readTree(json);
		dataNode = responseNode.get("articles");
		Article[] articles = new Article[dataNode.size()];

		for (int i = 0; i < dataNode.size(); i++) {

			articleNode = dataNode.get(i);
			tempArticle = Article.builder().author(articleNode.get("author").asText()).category(category)
					.title(articleNode.get("title").asText()).url(articleNode.get("url").asText())
					.description(articleNode.get("content").asText()).source(articleNode.get("source").asText())
					.imageUrl(articleNode.get("image").asText()).language(articleNode.get("language").asText())
					.countryOrigin(articleNode.get("country").asText())
					.publishedAt(articleNode.get("published_at").asText()).build();
			articles[i] = tempArticle;
		}
		
		return articles;
	}

	public String commaSeperateArray(Object[] arrayObj) {
		String newString = "";
		if (arrayObj != null) {
			for (int i = 0; i < arrayObj.length; i++) {

				newString += arrayObj[i];

				if (i != arrayObj.length - 1) {
					newString += ",";
				}
			}
		}
		return newString;
	}
}
