/**
 * 
 */
package com.service.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	Logger logger = LoggerFactory.getLogger(AppConfig.class);
	
	public static Map<String, Object> accessToken = new HashMap<>();

	public static void setToken(final String applicationName, final String token) {
		accessToken.put(applicationName, token);
	}

	public static String getToken(final String applicationName) {
		return accessToken.get(applicationName) != null ? accessToken.get(applicationName).toString() : null;
	}

	public static void removeAll() {
		accessToken.clear();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
