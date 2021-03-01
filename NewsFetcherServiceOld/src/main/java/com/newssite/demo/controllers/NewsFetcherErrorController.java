package com.newssite.demo.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NewsFetcherErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError() {
		return "Error404";
	}
	
    @Override
    public String getErrorPath() {
        return null;
    }
}
