package com.api.gateway.configs.filters;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class StripBasePathGatewayFilterFactory
		extends AbstractGatewayFilterFactory<StripPrefixGatewayFilterFactory.Config> {

	@Value("${spring.webflux.base-path}")
	private String basePath;

	public static final String PARTS_KEY = "parts";

	public StripBasePathGatewayFilterFactory() {
		super(StripPrefixGatewayFilterFactory.Config.class);
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(PARTS_KEY);
	}

	@Override
	public GatewayFilter apply(final StripPrefixGatewayFilterFactory.Config config) {
		return (exchange, chain) -> {
			final ServerHttpRequest req = exchange.getRequest();
			final String path = req.getURI().getRawPath();
			final String newPath = path.replaceFirst(this.basePath, "");

			final ServerHttpRequest request = req.mutate().path(newPath).contextPath(null).build();

			return chain.filter(exchange.mutate().request(request).build());
		};
	}
}
