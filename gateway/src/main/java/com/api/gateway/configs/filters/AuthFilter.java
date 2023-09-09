package com.api.gateway.configs.filters;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.api.gateway.configs.AppConfig;
import com.api.gateway.errorHandlers.AuthException;
import com.api.gateway.services.JwtTokenUtil;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter {

	final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Value("${header.auth.name}")
	private String authHeaderName;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Override
	public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
		String jwtToken="";
		String appName="";
		final String requestTokenHeader = exchange.getRequest().getHeaders().getFirst(this.authHeaderName);
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				final Long exp = jwtUtil.getExpirationDate(jwtToken);
				appName = jwtUtil.getappNameFromToken(jwtToken);
				if (exp != 0L && exp > -1) {
					final Date now = new Date();
					if (new Date(exp).before(now)) {
						System.out.println("Token is expired for  --> ");
						 throw new AuthException(AuthException.Codes.EA_003);
					}
				}
//			if (!jwtUtil.validateRole(newToken)) {
//			throw new AuthException(AuthException.Codes.EA_004);
//		}

		if (!jwtUtil.validateDisplayName(jwtToken, appName)) {
			throw new AuthException(AuthException.Codes.EA_004);
		}
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} 
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
			throw new AuthException(AuthException.Codes.EA_004);
		}

//		final String route = exchange.getRequest().getURI().getRawPath();
//
//		final String appName = exchange.getRequest().getHeaders().getFirst(this.authHeaderName);
//
//		if (appName == null || appName.isEmpty()) {
//			System.out.println("Application Name not found");
//			throw new AuthException(AuthException.Codes.EA_001);
//		}
//
//		if (!appName.equals(this.getApplications().get(route.split("/")[1]))) {
//			throw new AuthException(AuthException.Codes.EA_005);
//		}
//
//		String newToken = "";
//
//		// AppConfig.removeAll();
//		final String accessToken = AppConfig.getToken(appName);
//		if (null != accessToken && !accessToken.isEmpty()) {
//			System.out.println("Get existing token for --> " + appName);
//			newToken = AppConfig.getToken(appName);
//		} else {
//			System.out.println("Generate new token for --> " + appName);
//			newToken = jwtUtil.getToken(appName);
//		}
//
//		final Long exp = jwtUtil.getExpirationDate(newToken);
//		if (exp != 0L && exp > -1) {
//			final Date now = new Date();
//			if (new Date(exp).before(now)) {
//				System.out.println("Token is expired for  --> " + appName);
//				newToken = jwtUtil.getToken(appName);
//				System.out.println("Generate new token for --> " + appName);
//				// throw new AuthException(AuthException.Codes.EA_003);
//			}
//		}
//
////		if (!jwtUtil.validateRole(newToken)) {
////			throw new AuthException(AuthException.Codes.EA_004);
////		}
//
//		if (!jwtUtil.validateDisplayName(newToken, appName)) {
//			throw new AuthException(AuthException.Codes.EA_004);
//		}
//		System.out.println("********* "+newToken);
		final ServerHttpRequest request = exchange.getRequest().mutate().build();
		final ServerWebExchange exchange1 = exchange.mutate().request(request).build();
		return chain.filter(exchange1);
	}

	private Map<String, String> getApplications() {
		return Map.of("service-1", "SMBEmployeeServices_UAT_9908", "service-2", "SMBMerchantServices_UAT_9908");
	}
}
