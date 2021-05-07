package com.newssite.demo.resources;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@Service
public class NewsRetriever {

	private static final String NEWSFETCHERSERVICEURL = "http://localhost:8060/NewsFetcherService";
	private static final String NEWSPUBLISHENDPOINT = "/PublishNews";

	// Method to request the NewsFetcherService to publish news content into kafka
	public static void requestNewsProducer(String kafkaTopic, String topic, String country) {

		// Build a request based on the available variables
		(WebClient.builder().build()).get()
				.uri(NEWSFETCHERSERVICEURL, uriBuilder -> buildNewsRequest(uriBuilder, kafkaTopic, topic, country))
				.retrieve().bodyToMono(String.class).block();

	}

	// URI Builder for the provided variables
	private static URI buildNewsRequest(UriBuilder uriBuilder, String kafkaTopic, String topic, String country) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(NEWSPUBLISHENDPOINT);

		uriBuilder.queryParam("kafkaTopic", kafkaTopic);

		uriBuilder.queryParam("category", topic);

		uriBuilder.queryParam("country", country);

		return uriBuilder.build();
	}

}
