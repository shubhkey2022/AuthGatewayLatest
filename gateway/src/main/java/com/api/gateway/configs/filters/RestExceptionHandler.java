package com.api.gateway.configs.filters;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.api.gateway.errorHandlers.GlobalErrorAttributes;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class RestExceptionHandler extends AbstractErrorWebExceptionHandler {

	public RestExceptionHandler(final GlobalErrorAttributes g, final ApplicationContext applicationContext,
			final ServerCodecConfigurer serverCodecConfigurer) {
		super(g, new Resources(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

		final Map<String, Object> errorPropertiesMap = this.getErrorAttributes(request,
				ErrorAttributeOptions.defaults());

		return ServerResponse.status((HttpStatus) errorPropertiesMap.get("status"))
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(errorPropertiesMap));
	}
}
