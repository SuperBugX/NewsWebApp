package com.newssite.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "blacklisted_sources")
public class BlacklistedSource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotNull
	private String name;
	private String domain;
	@Column(name = "news_category_fk")
	@NotNull
	private String newsCategoryFK;
	@Column(name = "account_fk")
	@NotNull
	private String accountFK;

}
