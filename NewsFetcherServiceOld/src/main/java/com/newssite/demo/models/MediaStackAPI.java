package com.newssite.demo.models;

import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.resources.Article;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaStackAPI {
	

	//All possible API parameter values updated as of 26/01/21.
	
	//Attributes	
	//Registered API key
	private final String apiKey = "2fc31c34f76d68a481bacaf3b874a961";
	
	//API End points
	private final String liveNewsEndPoint = "http://api.mediastack.com/v1/news";
	
	/*
	 * 	Common request parameters
	 * 	Supported Categories:
	 * 	general
	 * 	business
	 * 	entertainment
	 * 	health
	 * 	science
	 * 	sports
	 * 	technology
	 */
	private String[] categories;
	
	/*
	 * Supported countries:
	 * ar - Argentina
	 * at - Austria
	 * au - Australia
	 * be - Belgium
	 * br - Brazil
	 * bg - Bulgaria
	 * ca - Canada
	 * cn - China
	 * co - Colombia
	 * cz -	Czech Republic
	 * eg - Egypt
	 * fr - France
	 * de - Germany
	 * gr - Greece
	 * hk - Hong Kong
	 * hu - Hungary
	 * in -	India 
	 * id - Indonesia
	 * ie - Ireland
	 * il - Israel
	 * it - Italy
	 * jp - Japan
	 * lv - Latvia
	 * lt - Lithuania
	 * my - Malaysia
	 * mx - Mexico
	 * ma - Morocco
	 * nl - Netherlands
	 * nz - New Zealand
	 * ng - Nigeria
	 * no - Norway
	 * ph - Philippines
	 * pl - Poland
	 * pt - Portugal
	 * ro - Romania
	 * sa - Saudi Arabia
	 * rs - Serbia
	 * sg - Singapore
	 * sk - Slovakia
	 * si -	Slovenia
	 * za - South Africa
	 * kr - South Korea
	 * se - Sweden
	 * ch - Switzerland
	 * tw - Taiwan
	 * th - Thailand
	 * tr - Turkey
	 * ae - UAE
	 * ua - Ukraine
	 * gb - United Kingdom
	 * us - United States
	 * ve - Venuzuela
	 */
	private String[] countries;
	/*
	 * 	Supported languages:
	 * 	ar - Arabic
	 *	de - German
	 *	en - English
	 * 	es - Spanish
	 *	fr - French
	 *	he - Hebrew
	 *	it - Italian
	 *	nl - Dutch
	 *	no - Norwegian
	 *	pt - Portuguese
	 *	ru - Russian
	 *	se - Swedish
	 *	zh - Chinese
	 *
	 */
	
	private String[] languages;
	private String[] keyWords;
	
	//Maximum pagination limit is 100, Default is 25
	private int paginationLimit;
	private int paginationOffset;
	
	//Not Available as parameters for specific article searching
	private String[] sources;
	
	/*
	 * Supported sorting values: 
	 * published_desc
	 * published_acs
	 * popularity
	 * 
	 */
	private String sortBy;
	
	//Methods
	
	public Article[] requestLiveArticles() {
		
		String requestParameters ="?access_key=" + apiKey;
		String apiJsonResponse = null;
		
		ObjectMapper jsonMapper = new ObjectMapper();
		JsonNode responseNode = null;
		JsonNode dataNode = null;
		
		Article[] articles = null;
		
		if(countries != null) {
			requestParameters += "&countries=";
			for(int i = 0; i < countries.length; i++) {
				
				requestParameters += countries[i];
				
				if(i != countries.length - 1) {
					requestParameters += ",";
				}
			}
		}
		
		if(languages != null) {
			requestParameters += "&languages=";
			for(int i = 0; i < languages.length; i++) {
				
				requestParameters += languages[i];
				
				if(i != languages.length - 1) {
					requestParameters += ",";
				}
			}
			
		}
		
		if(categories != null) {
			requestParameters += "&categories=";
			for(int i = 0; i < categories.length; i++) {
				
				requestParameters += categories[i];
				
				if(i != categories.length - 1) {
					requestParameters += ",";
				}
			}
		}
		
		if(keyWords != null) {
			
			requestParameters += "&keywords=";
			for(int i = 0; i < keyWords.length; i++) {
				
				requestParameters += keyWords[i];
				
				if(i != keyWords.length - 1) {
					requestParameters += ",";
				}
			}
		}
		
		if(sources != null) {
			requestParameters += "&sources=";
			for(int i = 0; i < sources.length; i++) {
				
				requestParameters += sources[i];
				
				if(i != sources.length - 1) {
					requestParameters += ",";
				}		
			}
		}
		
		if(sortBy != null) {
			requestParameters += "&sort=" + sortBy;
		}
		else {
			requestParameters += "&sort=popularity";
		}
		
		if(paginationLimit != 0) {
			requestParameters += "&limit=" + paginationLimit;
		}
		
		if(paginationOffset != 0) {
			requestParameters += "&offset=" + paginationOffset;
		}
		
		apiJsonResponse = (WebClient.builder().build())
			.get()
			.uri(liveNewsEndPoint + requestParameters)
			.retrieve()
			.bodyToMono(String.class)
			.block();
		
		try {
			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("data");
			articles = new Article[dataNode.size()];
			
			JsonNode articleNode = null;
			
			for(int i = 0; i < dataNode.size(); i++) {
				articleNode = dataNode.get(i);
				
				articles[i] = Article.builder().author(articleNode.get("author").asText())
								.category(articleNode.get("category").asText())
								.title(articleNode.get("title").asText())
								.url(articleNode.get("url").asText())
								.description(articleNode.get("description").asText())
								.source(articleNode.get("source").asText())
								.imageUrl(articleNode.get("image").asText())
								.language(articleNode.get("language").asText())
								.countryOrigin(articleNode.get("country").asText())
								.publishedAt(articleNode.get("published_at").asText())
								.build();
			}
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return articles;
		
	}
}
