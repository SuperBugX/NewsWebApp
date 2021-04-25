package com.newssite.demo.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import com.newssite.demo.configurations.KafkaConfig;
import com.newssite.demo.consumers.KafkaConsumerUtil;
import com.newssite.demo.consumers.TopicAckMessageListener;
import com.newssite.demo.resources.ClientSession;
import com.newssite.demo.resources.NewsRefresherTopicProcessor;
import com.newssite.demo.resources.NewsTopicProcessor;
import com.newssite.demo.resources.TopicSubscription;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController {

	// Attributes
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	private static final String NEWSFETCHERSERVICEURL = "http://localhost:8060/NewsFetcherService";
	private static final String NEWSPUBLISHENDPOINT = "/PublishNews2";

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
	public void requestNewsProducer(String kafkaTopic, String topic, String country) {

		// Build a request based on the available variables
		(WebClient.builder().build()).get()
				.uri(NEWSFETCHERSERVICEURL, uriBuilder -> buildNewsRequest(uriBuilder, kafkaTopic, topic, country))
				.retrieve().bodyToMono(String.class).block();

	}

	// URI Builder for the provided variables
	public URI buildNewsRequest(UriBuilder uriBuilder, String kafkaTopic, String topic, String country) {

		// Build a uri request with all of the attributes as query parameters inputs
		System.out.println("Country" + country + "Category" + topic + "KAFKA" + kafkaTopic);

		uriBuilder.path(NEWSPUBLISHENDPOINT);

		uriBuilder.queryParam("kafkaTopic", kafkaTopic);

		uriBuilder.queryParam("category", topic);

		uriBuilder.queryParam("country", country);

		System.out.println("I GOT" + uriBuilder.build().toString());

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
			if (subscriptionDestination != null && !subscriptionDestination.contains("/user")) {

				// Get the clients session ID
				String sessionID = event.getUser().getName();
				ClientSession sessionEntry = (ClientSession) activeSessions.get(sessionID);
				boolean isSubscribed = false;

				// Check if the client already has a session as indicated in the data structure
				if (sessionEntry != null) {
					// Check if the existing client is already subscribed to a topic
					if (!sessionEntry.getStompSubscriptions().contains(subscriptionDestination)) {
						sessionEntry.addSubscription(subscriptionDestination);
					} else {
						isSubscribed = true;
					}
				} else {
					// Create a new session instance for the new client connection
					activeSessions.put(sessionID, new ClientSession(sessionID,
							new LinkedList<String>(Arrays.asList(subscriptionDestination))));
				}

				// Parse the string URI as a Map with String Parameter keys and List<String>
				// values as parameter values
				Map<String, List<String>> parameters = getURIParam(subscriptionDestination);
				String country = "";

				if (parameters != null) {
					List<String> languageParam = parameters.get("lang");
					List<String> countryParam = parameters.get("country");

					// Check if any parameters were provided
					if (countryParam != null && countryParam.get(0) != null) {
						country = countryParam.get(0);
					}
				}

				// Remove the desired STOMP prefix
				String topicName = StringUtils.substringBetween(subscriptionDestination, "/topic/", "?");
				String kafkaTopic = topicName + country;

				// Depending on if the client is already subscribed, create a new topic
				// subscription and kafka consumer
				if (!isSubscribed) {
					TopicSubscription topicEntry = (TopicSubscription) activeTopics.get(subscriptionDestination);
					// Check if the topic already exists base don a data structure and thus has a
					// kafka consumer already
					if (topicEntry != null) {
						// Increment the amount subscriptions that exist for the topic
						topicEntry.incrementSubscriptions();

						// Start a new consumer for the new topic
						KafkaConsumerUtil
								.createTemporaryConsumer(kafkaTopic,
										new TopicAckMessageListener(new NewsRefresherTopicProcessor(
												subscriptionDestination, sessionID, simpMessagingTemplate), false),
										consumerConfig, 10000);
					} else {

						if (KafkaConfig.topicExists(kafkaTopic)) {

							activeTopics.put(subscriptionDestination,
									new TopicSubscription(kafkaTopic, subscriptionDestination));

							// Start a new consumer for the new topic
							KafkaConsumerUtil
									.createTemporaryConsumer(kafkaTopic,
											new TopicAckMessageListener(new NewsRefresherTopicProcessor(
													subscriptionDestination, sessionID, simpMessagingTemplate), false),
											consumerConfig, 10000);

						} else {
							activeTopics.put(subscriptionDestination,
									new TopicSubscription(kafkaTopic, subscriptionDestination));

							// Start a new consumer for the new topic
							KafkaConsumerUtil.startOrCreateConsumers(kafkaTopic,
									new TopicAckMessageListener(
											new NewsTopicProcessor(subscriptionDestination, simpMessagingTemplate),
											true),
									1, consumerConfig);

							// Request for news content from the NewsFetcherService to be published into
							// kafka
							requestNewsProducer(kafkaTopic, topicName, country);
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

		// Get the clients session ID
		String sessionID = event.getUser().getName();

		// Check if a session ID exists and was found
		if (sessionID != null) {

			ClientSession session = activeSessions.get(sessionID);
			// Check if a session instance was found in a data structure
			if (session != null) {

				// Get the clients list of subscriptions
				List<String> subscriptions = session.getStompSubscriptions();

				// For each topic, decrements its amount of subscribers by one and check if a
				// subscription should be deleted
				// and thus the corresponding kafka consumer terminated
				for (String subscription : subscriptions) {
					TopicSubscription topic = activeTopics.get(subscription);
					if (topic != null) {
						topic.decrementSubscriptions();
						if (topic.getSubscriptions() <= 0) {
							KafkaConsumerUtil.stopConsumer(topic.getKafkaTopic());
							activeTopics.remove(subscription);
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

		// Get the clients session ID
		String sessionID = event.getUser().getName();

		// Check if a session ID was found
		if (sessionID != null) {

			ClientSession sessionEntry = (ClientSession) activeSessions.get(sessionID);

			// Check if a session instance exists
			if (sessionEntry != null) {

				String stompSubscription = event.getMessage().getHeaders().get("simpSubscriptionId").toString();

				// Check if a desired subscription/topic to unsubscribe from exists and whether
				// it was successfully removed from the ClientSession instance
				if (sessionEntry.removeSubscription(stompSubscription)) {

					// Get the topic/subscription instance
					TopicSubscription topicEntry = (TopicSubscription) activeTopics.get(stompSubscription);

					// Check if the desired topic exists
					if (topicEntry != null) {
						topicEntry.decrementSubscriptions();

						// Check if the number of subscribers is below zero
						if (topicEntry.getSubscriptions() <= 0) {
							// Delete the topic from the data structure and terminate the corresponding
							// consumer
							KafkaConsumerUtil.stopConsumer(topicEntry.getKafkaTopic());
							activeTopics.remove(stompSubscription);
						}
					}
				}
			}

		}

	}
}
