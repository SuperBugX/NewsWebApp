package com.newssite.demo.models;

import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.abstarct.AbstractNewsAPI;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.interfaces.TopNewsAPI;
import com.newssite.demo.resources.Article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaStack extends AbstractNewsAPI implements TopNewsAPI {

	// All possible API parameter values updated as of 26/01/21.

	// Attributes
	// Registered API key
	private final String APIKEY = "2fc31c34f76d68a481bacaf3b874a961";

	// API Host URI
	private final String HOST = "http://api.mediastack.com";
	// Live News endPoint
	private final String LIVENEWS = "/v1/news";

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
	 */

	private String language;

	// General keywords to be used
	private String[] keyWords;

	// Maximum pagination limit is 100, Default is 25
	private int paginationLimit;
	private int paginationOffset;

	// Contains news sources (organisations)
	private String[] sources;

	// Supported sorting values: published_desc, published_acs, popularity
	private String sortBy;

	// Methods
	public String getLatestNews() throws NewsAPIResponseErrorException, NewsAPIJSONException {

		String apiJsonResponse = null;
		JsonNode responseNode = null;
		JsonNode dataNode = null;
		ObjectMapper jsonMapper = new ObjectMapper();

		// Build a request based on the available variables and get a JSON response
		apiJsonResponse = (WebClient.builder().build()).get()
				.uri(HOST, uriBuilder -> buildLiveArticlesRequest(uriBuilder)).retrieve().bodyToMono(String.class)
				.block();

		try {
			// Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("error");

			// Check if a error response was provided
			if (dataNode != null) {
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

	public URI buildLiveArticlesRequest(UriBuilder uriBuilder) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(LIVENEWS).queryParam("access_key", APIKEY);

		if (country != null && !country.equals("")) {
			uriBuilder.queryParam("country", country);
		}

		if (language != null && !language.equals("")) {
			uriBuilder.queryParam("language", language);
		}

		if (category != null && !category.equals("")) {
			uriBuilder.queryParam("category", category);
		}

		if (sources != null && !sources.equals(new String[] {})) {
			uriBuilder.queryParam("sources", commaSeperateArray(sources));
		}

		if (keyWords != null && !keyWords.equals(new String[] {})) {
			uriBuilder.queryParam("keywords", commaSeperateArray(keyWords));
		}

		if (paginationLimit != 0) {
			uriBuilder.queryParam("pageSize", paginationLimit);
		}

		if (paginationOffset != 0) {
			uriBuilder.queryParam("page", paginationOffset);
		}

		if (sortBy != null) {
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
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new NewsAPIJSONException(e.getMessage());
		}
		dataNode = responseNode.get("data");
		Article[] articles = new Article[dataNode.size()];

		// Place the articles in the correct data-structures
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
		return articles;
	}
}
