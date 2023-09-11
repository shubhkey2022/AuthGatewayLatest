package com.service.config;

import java.io.Serializable;
import java.util.LinkedHashMap;

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

import com.service.errorHandlers.AuthException;

@Component
public class JwtTokenUtil implements Serializable {

	@Value("${microsoft.url}")
	private String url;

	@Value("${microsoft.grantType}")
	private String grantType;

	@Value("${microsoft.scope}")
	private String scope;

	@Value("${microsoft.clientIdEmployee}")
	private String clientIdEmployee;

	@Value("${microsoft.secretEmployee}")
	private String secretEmployee;

	@Autowired
	private RestTemplate restTemplate;

	private static final long serialVersionUID = 6366567108855027683L;

	public String getToken(final String applicationName) {

		String clientId = "";
		String secret = "";

		clientId = clientIdEmployee;
		secret = secretEmployee;

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
