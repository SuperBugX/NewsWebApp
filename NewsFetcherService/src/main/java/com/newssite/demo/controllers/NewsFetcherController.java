package com.newssite.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.models.MediaStack;
import com.newssite.demo.models.MediaStack.MediaStackBuilder;
import com.newssite.demo.models.NewsAPI;
import com.newssite.demo.models.NewsAPI.NewsAPIBuilder;
import com.newssite.demo.resources.Article;
import com.newssite.demo.resources.ErrorTemplate;

@RestController
@RequestMapping("/NewsFetcherService")
public class NewsFetcherController {

	// Attributes
	@Autowired
	MediaStackBuilder mediaStackBuilder;
	
	@Autowired 
	NewsAPIBuilder newsAPIBuilder;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	// Methods
	@RequestMapping("/RetrieveNews")
	public String retrieveNews(@RequestParam("categories") String[] categories,
			@RequestParam("countries") String[] countries, @RequestParam("languages") String[] languages,
			@RequestParam("keywords") String[] keywords, @RequestParam("limit") int limit,
			@RequestParam("offset") int offset, @RequestParam("sources") String[] sources,
			@RequestParam("sort") String sort) {

		ErrorTemplate errorTemplate;

		// Build the API request parameters
		MediaStack api = mediaStackBuilder.countries(countries).categories(categories).languages(languages)
				.sources(sources).keyWords(keywords).paginationLimit(limit).paginationOffset(offset).sortBy(sort)
				.build();

		try {
			// Perform the API request and return the JSON result
			return api.getLiveArticles();
		} catch (NewsAPIResponseErrorException e) {
			// TODO Auto-generated catch block
			errorTemplate = new ErrorTemplate("api-error-responses", "All APIs produced errors in their responses");
			return errorTemplate.toJSON();

		} catch (NewsAPIJSONException e) {
			// TODO Auto-generated catch block
			errorTemplate = new ErrorTemplate("json-error", "Error processing JSON data");
			return errorTemplate.toJSON();
		}
	}

	@RequestMapping("/PublishNews")
	public void publishNews(@RequestParam("kafkaTopic") String kafkaTopic, @RequestParam("category") String category,
			@RequestParam("country") String country, @RequestParam("language") String language) {

		System.out.println("Country" + country + "Category" + category + "Language" + language + "KAFKA" + kafkaTopic);
		// Build the API request parameters
		MediaStack api = mediaStackBuilder.countries(new String[] { country }).categories(new String[] { category })
				.languages(new String[] { language }).build();

		try {
			// Perform API request
			String apiJsonResponse = api.getLiveArticles();

			ObjectMapper jsonMapper = new ObjectMapper();
			JsonNode responseNode;
			JsonNode dataNode;
			JsonNode articleNode;
			Article tempArticle;

			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("data");

			// Place the articles in the correct data-structures
			for (int i = 0; i < dataNode.size(); i++) {

				articleNode = dataNode.get(i);
				tempArticle = Article.builder().author(articleNode.get("author").asText())
						.category(articleNode.get("category").asText()).title(articleNode.get("title").asText())
						.url(articleNode.get("url").asText()).description(articleNode.get("description").asText())
						.source(articleNode.get("source").asText()).imageUrl(articleNode.get("image").asText())
						.language(articleNode.get("language").asText())
						.countryOrigin(articleNode.get("country").asText())
						.publishedAt(articleNode.get("published_at").asText()).build();

				kafkaTemplate.send(kafkaTopic.toString(), jsonMapper.writeValueAsString(tempArticle));
				System.out.println("I SENT" + kafkaTopic);

			}
		} catch (NewsAPIResponseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (NewsAPIJSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/PublishNews2")
	public void publishNews2(@RequestParam("kafkaTopic") String kafkaTopic, @RequestParam("category") String category,
			@RequestParam("country") String country, @RequestParam("language") String language) {

		System.out.println("Country" + country + "Category" + category + "Language" + language + "KAFKA" + kafkaTopic);
		// Build the API request parameters
		NewsAPI api = newsAPIBuilder.country(country).category(category).language(language).build();

		try {
			// Perform API request
			String apiJsonResponse = api.getLiveArticles();

			ObjectMapper jsonMapper = new ObjectMapper();
			JsonNode responseNode;
			JsonNode dataNode;
			JsonNode articleNode;
			Article tempArticle;

			responseNode = jsonMapper.readTree(apiJsonResponse);
			dataNode = responseNode.get("articles");

			// Place the articles in the correct data-structures
			for (int i = 0; i < dataNode.size(); i++) {

				articleNode = dataNode.get(i);
				tempArticle = Article.builder().author(articleNode.get("author").asText())
						.category(category).title(articleNode.get("title").asText())
						.url(articleNode.get("url").asText()).description(articleNode.get("content").asText())
						.source(articleNode.get("source").asText()).imageUrl(articleNode.get("image").asText())
						.language(articleNode.get("language").asText())
						.countryOrigin(articleNode.get("country").asText())
						.publishedAt(articleNode.get("published_at").asText()).build();

				kafkaTemplate.send(kafkaTopic.toString(), jsonMapper.writeValueAsString(tempArticle));
				System.out.println("I SENT" + kafkaTopic);

			}
		} catch (NewsAPIResponseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (NewsAPIJSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


//	@RequestMapping("/PublishNews")
//	public void publishNews(@RequestParam("categories") String[] categories,
//			@RequestParam("countries") String[] countries, @RequestParam("languages") String[] languages,
//			@RequestParam("keywords") String[] keywords, @RequestParam("limit") int limit,
//			@RequestParam("offset") int offset, @RequestParam("sources") String[] sources,
//			@RequestParam("sort") String sort) {
//
}
