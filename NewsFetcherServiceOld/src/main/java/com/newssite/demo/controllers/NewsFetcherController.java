package com.newssite.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.newssite.demo.models.MediaStackAPI;
import com.newssite.demo.models.MediaStackAPI.MediaStackAPIBuilder;
import com.newssite.demo.resources.ArticlesPayload;

@Controller
@RequestMapping("/NewsFetcherService")
public class NewsFetcherController {
	
	@Autowired
	MediaStackAPIBuilder mediaStackBuilder;
	
	@RequestMapping("/Demonstration")
	public String returnIndex(Model model) {

		String[] categories = {"general", "sports"};
		String[] countries = {"gr"};
		String[] languages = {"en"};
		String[] keyWords = null;
		String[] sources = {"bbc"}; 
		
		MediaStackAPI api = mediaStackBuilder.countries(countries).categories(categories).build();
		model.addAttribute("NewsData", api.requestLiveArticles());
		
		return "Index";
	}
	
	@RequestMapping("/RetrieveNews/Categories/{categories}/Countries/{countries}/Languages/{languages}/Keywords/{keywords}/PaginationLimit/{limit}/PaginationOffset/{offset}/Sources/{sources}/SortBy/{sort}")
	public ArticlesPayload retrieveNews(@PathVariable("categories") String[] categories, @PathVariable("countries") String[] countries, @PathVariable("languages") String[] languages, @PathVariable("keywords") String[] keywords, @PathVariable("limit") int limit, @PathVariable("offset") int offset, @PathVariable("sources") String[] sources,@PathVariable("sort") String sort) {
		
		MediaStackAPI api = mediaStackBuilder.countries(countries)
				.categories(categories)
				.languages(languages)
				.sources(sources)
				.keyWords(keywords)
				.paginationLimit(limit)
				.paginationOffset(offset)
				.sortBy(sort)
				.build();
		
		return new ArticlesPayload(api.requestLiveArticles());
		
	}
}
