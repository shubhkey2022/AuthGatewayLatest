/**
 * 
 */
package com.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.service.config.AppConfig;
import com.service.config.JwtTokenUtil;
import com.service.constants.AppEntityCodes;
import com.service.errorHandlers.AuthException;

@RestController
@RequestMapping(AppEntityCodes.EMPLOYEE)
public class EmployeeController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JwtTokenUtil jwtUtil;

	public EmployeeController() {
	}

	@GetMapping("{id}")
	public Map<String, Object> getEmployee(final HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @PathVariable("id") final Long id) {

		Map<String, Object> returnMap = new HashMap<>();

		if (id == 1) {
			returnMap.putAll(Map.of("id", "1", "firstName", "Abc", "lastName", "Efg", "contactNo", "1234567890"));
		} else if (id == 2) {
			returnMap.putAll(Map.of("id", "2", "firstName", "DEF", "lastName", "ABCD", "contactNo", "9876543210"));
		} else if (id == 3) {
			returnMap.putAll(Map.of("id", "3", "firstName", "XYz", "lastName", "Test", "contactNo", "4561237890"));
		}

		// get address from service-2
		final String validationUrl = "http://localhost:8180/gateway/validateToken";
		final String url = "http://localhost:8182/service-2/employee/address/" + id;

		String appName = "";
		if (httpServletRequest.getHeader("ApplicationName") != null) {
			appName = httpServletRequest.getHeader("ApplicationName");
		}

		String authHeader=generateToken(appName);
		boolean resultFlag=false;
		
		try {
			resultFlag = validateToken(validationUrl, authHeader,appName);
			if(resultFlag) {
				final Map<String, Object> address = getAddressFromService2(url);

				returnMap.put("address", address);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		return returnMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> getAddressFromService2(final String url) {
		final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
		//headers.add("Authorization", "Bearer " + authHeader);

		try {
			final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
			final ResponseEntity<Map> response = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					Map.class);
			return response.getBody();
		} catch (HttpStatusCodeException e) {
//			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
//				getAddressFromService2(url);
//			}
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean validateToken(final String url, final String authHeader,String appName) {
		boolean flag=true;
		final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
		headers.add("Authorization", "Bearer " + authHeader);
		
		try {
			final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
			final ResponseEntity<Map> response = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					Map.class);
			Map<String, Object> validationResponse = response.getBody();
			if(validationResponse != null) {
				System.out.println(validationResponse);
				if(validationResponse.get("result") != null) {
					Boolean result=(Boolean)validationResponse.get("result");
					String errorMessage=validationResponse.get("error").toString();
					if(!result && errorMessage!= null && errorMessage.equalsIgnoreCase(AuthException.getMessage(AuthException.Codes.EA_003))) {
						String authHeaderNew=generateToken(appName);
						result=validateToken(url, authHeaderNew, appName);
					}
					flag=result;
					
				}
			}
		} catch (HttpStatusCodeException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@GetMapping("emp/{id}")
	public Map<String, Object> getEmployeeByAddress(final HttpServletRequest httpServletRequest,
			@PathVariable("id") final Long id) {

		final Map<String, Object> returnMap = new HashMap<>();

		if (id == 1) {
			returnMap.putAll(Map.of("id", "1", "firstName", "Abc", "lastName", "Efg", "contactNo", "1234567890"));
		} else if (id == 2) {
			returnMap.putAll(Map.of("id", "2", "firstName", "DEF", "lastName", "ABCD", "contactNo", "9876543210"));
		} else if (id == 3) {
			returnMap.putAll(Map.of("id", "3", "firstName", "XYz", "lastName", "Test", "contactNo", "4561237890"));
		}

		return returnMap;
	}
	
	private String generateToken(String appName){
		String authHeader = AppConfig.getToken(appName);
		if (null != authHeader && !authHeader.isEmpty()) {
			System.out.println("Get existing token for --> " + appName);
			authHeader = AppConfig.getToken(appName);
		} else {
			System.out.println("Generate new token for --> " + appName);
			authHeader = jwtUtil.getToken(appName);
		}
		System.out.println(authHeader);
		return authHeader;
	}
}
