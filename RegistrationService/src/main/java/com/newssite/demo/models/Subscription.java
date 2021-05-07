package com.newssite.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String domain;

	@ManyToOne
	@JoinColumn(name="user_fk")
	private User userFK;

	@ManyToOne
	@JoinColumn(name="news_topic_fk")
	private NewsTopic newsTopicFK;
}
