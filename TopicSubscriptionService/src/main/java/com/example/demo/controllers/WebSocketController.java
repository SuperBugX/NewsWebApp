package com.example.demo.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import com.example.demo.configurations.KafkaConfig;
import com.example.demo.consumers.CustomMessageListener;
import com.example.demo.consumers.KafkaConsumerUtil;
import com.example.demo.resources.ClientSession;
import com.example.demo.resources.NewsTopicProcessor;
import com.example.demo.resources.TopicSubscription;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {

	// Attributes
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	private static final String NEWSFETCHERSERVICEURL = "http://localhost:8060/NewsFetcherService";
	private static final String NEWSPUBLISHENDPOINT = "/PublishNews";

	private Map<String, Object> consumerConfig = KafkaConfig.getConsumerConfig();
	private Map<String, TopicSubscription> activeTopics = new HashMap<String, TopicSubscription>(15);
	private Map<String, ClientSession> activeSessions = new HashMap<String, ClientSession>(50);

	// Methods

	/*
	 * Method returns a Map with String keys And List<String> Values. Each key
	 * represents a parameter retrieved from the provided String uri and the value
	 * is a string list with the found values for the corresponding parameter key
	 * NOTE: The received method parameter uri is not decoded in the process
	 */
	public Map<String, List<String>> getURIParam(String uri) {
		return UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
	}

	// Method to request the NewsFetcherService to publish news content into kafka
	public void requestNews(String topic, String language, String country) {

		// Build a request based on the available variables
		(WebClient.builder().build()).get().uri(NEWSFETCHERSERVICEURL,
				uriBuilder -> buildNewsRequest(uriBuilder, topic, language, country));

	}

	// URI Builder for the provided variables
	public URI buildNewsRequest(UriBuilder uriBuilder, String topic, String language, String country) {

		// Build a uri request with all of the attributes as query parameters inputs

		uriBuilder.path(NEWSPUBLISHENDPOINT);

		uriBuilder.queryParam("category", topic);

		uriBuilder.queryParam("language", language);

		uriBuilder.queryParam("country", country);

		return uriBuilder.build();
	}

	// Event Listeners
	@EventListener
	public void onApplicationEvent(SessionSubscribeEvent event) {

		// Get the headers from the received frame and specifically get the
		// "nativeHeaders" header as a Map
		Map<String, Object> nativeHeaders = (Map<String, Object>) event.getMessage().getHeaders().get("nativeHeaders");

		// Check if the nativeHeaders exists on the frame
		if (nativeHeaders != null) {
			// Get the intended destination/topic of the frame
			String subscriptionDestination = ((ArrayList<String>) nativeHeaders.get("destination")).get(0);

			// Check if the destination exists
			if (subscriptionDestination != null) {

				// Remove the desired STOMP prefix
				String topic = subscriptionDestination.replaceFirst("/topic/", "");
				// Get the sessions attributes variable from the received frame
				// The variable is used to retrieve the current clients session ID for
				// identification purposes
				Map<String, String> sessionAttributes = (ConcurrentHashMap<String, String>) event.getMessage()
						.getHeaders().get("simpSessionAttributes");

				// Check if the sessions attributes variable could be found from the header
				if (sessionAttributes != null) {

					// Get the clients session ID
					String sessionID = sessionAttributes.get("sessionId");
					ClientSession sessionEntry = (ClientSession) activeSessions.get(sessionID);
					boolean isSubscribed = false;

					// Check if the client already has a session as indicated in the data structure
					if (sessionEntry != null) {
						// Check if the existing client is already subscribed to a topic
						if (!sessionEntry.getSubscriptions().contains(topic)) {
							sessionEntry.addSubscription(topic);
						} else {
							isSubscribed = true;
						}
					} else {
						// Create a new session instance for the new client connection
						activeSessions.put(sessionID,
								new ClientSession(sessionID, new LinkedList<String>(Arrays.asList(topic))));
					}

					// Depending on if the client is already subscribed, create a new topic
					// subscription and kafka consumer
					if (!isSubscribed) {
						TopicSubscription topicEntry = (TopicSubscription) activeTopics.get(topic);
						// Check if the topic already exists base don a data structure and thus has a
						// kafka consumer already
						if (topicEntry != null) {
							// Increment the amount subscriptions that exist for the topic
							topicEntry.incrementSubscriptions();
						} else {

							activeTopics.put(topic, new TopicSubscription(topic));

							// Parse the string URI as a Map with String Parameter keys and List<String>
							// values as parameter values
							Map<String, List<String>> parameters = getURIParam(subscriptionDestination);
							String country = "";
							String language = "";

							if (parameters != null) {
								List<String> languageParam = parameters.get("lang");
								List<String> countryParam = parameters.get("country");

								// Check if any parameters were provided
								if (languageParam != null && languageParam.get(0) != null) {
									language = languageParam.get(0);
								}

								if (countryParam != null && countryParam.get(0) != null) {
									country = countryParam.get(0);
								}
							}

							// Request for news content from the NewsFetcherService to be published into kafka
							requestNews(topic, language, country);

							// Start a new consumer for the new topic
							KafkaConsumerUtil.startOrCreateConsumers(topic,
									new CustomMessageListener(new NewsTopicProcessor(topic, simpMessagingTemplate)), 1,
									consumerConfig);
						}
					}
				}
			}
		}
	}

	@EventListener
	public void onApplicationEvent(SessionDisconnectEvent event) {

		// Get the sessions attributes variable from the received frame
		// The variable is used to retrieve the current clients session ID for
		// identification purposes
		Map<String, String> attributes = (ConcurrentHashMap<String, String>) event.getMessage().getHeaders()
				.get("simpSessionAttributes");
		String sessionID = null;

		// Check if the attributes variable exists
		if (attributes != null) {
			// Get the clients session ID if it exists
			sessionID = attributes.get("sessionId");
		}

		// Check if a session ID exists and was found
		if (sessionID != null) {

			ClientSession session = activeSessions.get(sessionID);
			// Check if a session instance was found in a data structure
			if (session != null) {

				// Get the clients list of subscriptions
				List<String> subscriptions = session.getSubscriptions();

				// For each topic, decrements its amount of subscribers by one and check if a
				// subscription should be deleted
				// and thus the corresponding kafka consumer terminated
				for (String subscription : subscriptions) {
					TopicSubscription topic = activeTopics.get(subscription);
					if (topic != null) {
						topic.decrementSubscriptions();
						if (topic.getSubscriptions() <= 0) {
							activeTopics.remove(subscription);
							KafkaConsumerUtil.stopConsumer(subscription);
						}
					}
				}

				// Remove the session instance
				activeSessions.remove(sessionID);
			}
		}
	}

	@EventListener
	public void onApplicationEvent(SessionUnsubscribeEvent event) {

		// Get the sessions attributes variable from the received frame
		// The variable is used to retrieve the current clients session ID for
		// identification purposes
		Map<String, String> attributes = (ConcurrentHashMap<String, String>) event.getMessage().getHeaders()
				.get("simpSessionAttributes");
		String sessionID = null;

		// Check if the attributes variable exists
		if (attributes != null) {
			// Get the clients session ID if it exists
			sessionID = attributes.get("sessionId");

			// Check if a session ID was found
			if (sessionID != null) {

				ClientSession sessionEntry = (ClientSession) activeSessions.get(sessionID);

				// Check if a session instance exists
				if (sessionEntry != null) {

					String subscription = event.getMessage().getHeaders().get("simpSubscriptionId").toString()
							.replaceFirst("/topic/", "");

					// Check if a desired subscription/topic to unsubscribe from exists and whether
					// it was successfully removed from the ClientSession instance
					if (subscription != null && sessionEntry.removeSubscription(subscription)) {

						// Get the topic/subscription instance
						TopicSubscription topicEntry = (TopicSubscription) activeTopics.get(subscription);

						// Check if the desired topic exists
						if (topicEntry != null) {
							topicEntry.decrementSubscriptions();

							// Check if the number of subscribers is below zero
							if (topicEntry.getSubscriptions() <= 0) {
								// Delete the topic from the data structure and terminate the corresponding
								// consumer
								activeTopics.remove(subscription);
								KafkaConsumerUtil.stopConsumer(subscription);
							}
						}
					}
				}

			}
		}

	}
}
