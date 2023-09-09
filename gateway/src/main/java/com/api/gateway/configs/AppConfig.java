package com.api.gateway.configs;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {

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
}
