package com.newssite.demo.resources;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticlesPayload {
	
	private Article[] articles;

}
