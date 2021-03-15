package com.newssite.demo.models;

import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.exceptions.MediaStackJSONException;
import com.newssite.demo.exceptions.MediaStackResponseErrorException;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaStack {

	// All possible API parameter values updated as of 26/01/21.

	// Attributes
	// Registered API key
	private final String APIKEY = "2fc31c34f76d68a481bacaf3b874a961";

	//API Host URI
	private final String HOST = "http://api.mediastack.com";
	//Live News endPoint
	private final String LIVENEWS = "/v1/news";

	/*
	 * Supported Categories: general, business,
	 * entertainment, health, science, sports technology,
	 */
	private String[] categories;

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

	private String[] countries;
	/*
	 * Supported languages: ar - Arabic de - German en - English es - Spanish fr -
	 * French he - Hebrew it - Italian nl - Dutch no - Norwegian pt - Portuguese ru
	 * - Russian se - Swedish zh - Chinese
	 */

	private String[] languages;
	private String[] keyWords;

	// Maximum pagination limit is 100, Default is 25
	private int paginationLimit;
	private int paginationOffset;

	//Contains news sources (organisations)
	private String[] sources;

	/*
	 * Supported sorting values: published_desc, published_acs, popularity
	 */
	private String sortBy;

	// Methods
	public String getLiveArticles() throws MediaStackResponseErrorException, MediaStackJSONException {

		String apiJsonResponse = null;
		JsonNode responseNode = null;
		JsonNode dataNode = null;
		ObjectMapper jsonMapper = new ObjectMapper();
		
		//Build a request based on the available variables and get a JSON response
		apiJsonResponse = (WebClient.builder().build()).get().uri(HOST, uriBuilder -> buildLiveNewsRequest(uriBuilder))
				.retrieve().bodyToMono(String.class).block();

		try {
			//Parse the JSON response
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("error");

			//Check if a error response was provided
			if (dataNode != null) {
				throw new MediaStackResponseErrorException("Received the error:" + dataNode.asText());
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new MediaStackJSONException(e.getMessage());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new MediaStackJSONException(e.getMessage());
		}

		//Return the JSON list of articles
		return apiJsonResponse;
	}

	public URI buildLiveNewsRequest(UriBuilder uriBuilder) {
		
		//Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(LIVENEWS).queryParam("access_key", APIKEY);

		uriBuilder.queryParam("countries", commaSeperateArray(countries));

		uriBuilder.queryParam("languages", commaSeperateArray(languages));

		uriBuilder.queryParam("categories", commaSeperateArray(categories));

		uriBuilder.queryParam("keywords", commaSeperateArray(keyWords));

		uriBuilder.queryParam("sources", commaSeperateArray(sources));

		uriBuilder.queryParam("limit", paginationLimit);

		uriBuilder.queryParam("offset", paginationOffset);

		if (sortBy != null) {
			uriBuilder.queryParam("sort", sortBy);
		} else {
			uriBuilder.queryParam("sort", "published_desc");
		}

		return uriBuilder.build();
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
