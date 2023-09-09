package com.service.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.constants.Constants;
import com.service.constants.Constants.JwtKeys;
import com.service.errorHandlers.AuthException;

@Component
public class JwtTokenUtil implements Serializable {

	@Value("${microsoft.url}")
	private String url;

	@Value("${microsoft.grantType}")
	private String grantType;

	@Value("${microsoft.scope}")
	private String scope;

	@Value("${microsoft.clientIdMerchant}")
	private String clientIdMerchant;

	@Value("${microsoft.secretMerchant}")
	private String secretMerchant;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6366567108855027683L;

	public Long getExpirationDate(final String token) {
		final Map<String, Object> claims = getClaim(token);
		if (null != claims) {
			return claims.get(JwtKeys.EXPIRATION) != null ? (objectToLong(claims.get(JwtKeys.EXPIRATION)) * 1000) : 0L;
		}
		return 0L;
	}

	public boolean validateDisplayName(final String token, final String appName) {
		final Map<String, Object> claims = getClaim(token);
		if (null != claims) {
			if (null != claims.get(JwtKeys.APP_DISPLAY_NAME)) {
				final String displayName = claims.get(JwtKeys.APP_DISPLAY_NAME).toString();
				if (appName.equalsIgnoreCase(displayName)) {
					return true;
				}
//				for (final String applicationName : Constants.APPLCIATION_DISPLAY_NAMES) {
//					if (applicationName.equalsIgnoreCase(displayName)) {
//						return true;
//					}
//				}
			}
			return false;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean validateRole(final String token) {
		final Map<String, Object> claims = getClaim(token);
		if (null != claims) {
			if (null != claims.get(JwtKeys.ROLES)) {
				final List<String> roles = (ArrayList<String>) claims.get(JwtKeys.ROLES);
				if ((roles.stream().anyMatch(x -> x.equalsIgnoreCase(Constants.ROLES)))
						|| (roles.stream().anyMatch(x -> x.equalsIgnoreCase(Constants.ROLES))
								&& roles.stream().anyMatch(x -> x.equalsIgnoreCase(Constants.ROLES1)))) {
					return true;
				}
			}
			return false;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getClaim(final String token) {
		final String[] chunks = token.split("\\.");
		final Base64.Decoder decoder = Base64.getUrlDecoder();
		final String payload = new String(decoder.decode(chunks[1]));
		try {
			return new ObjectMapper().readValue(payload, HashMap.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Long objectToLong(final Object obj) {
		return Long.parseLong(obj.toString());
	}

	public String getToken(final String applicationName) {

		String clientId = "";
		String secret = "";

		clientId = clientIdMerchant;
		secret = secretMerchant;

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", clientId);
		map.add("grant_type", grantType);
		map.add("scope", scope);
		map.add("client_secret", secret);

		final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

		try {
			final ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
			if (response != null) {
				@SuppressWarnings("unchecked")
				final LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) response.getBody();
				AppConfig.setToken(applicationName, body.get("access_token").toString());
				return body.get("access_token").toString();
			}
		} catch (Exception e) {
			throw new AuthException(AuthException.Codes.EA_004);
		}
		return null;
	}
}
