package com.service.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
	@GetMapping("")
	public String service2(final HttpServletRequest httpServletRequest) {
		return "service 2 is running";
	}
}