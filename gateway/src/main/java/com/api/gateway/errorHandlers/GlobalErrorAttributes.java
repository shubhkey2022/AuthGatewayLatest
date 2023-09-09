package com.api.gateway.errorHandlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(final ServerRequest request, final ErrorAttributeOptions options) {
//		final Map<String, Object> map = super.getErrorAttributes(request, options);
		final Map<String, Object> map = new HashMap<>();
		if (this.getError(request) instanceof AuthException) {
			map.put("status", HttpStatus.UNAUTHORIZED);
		} else if (this.getError(request) instanceof RuntimeException) {
			map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		map.put("message", this.getError(request).getMessage());
		return map;
	}
}
