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
import org.springframework.web.client.RestTemplate;

import com.service.constants.AppEntityCodes;

@RestController
@RequestMapping(AppEntityCodes.EMPLOYEE)
public class EmployeeController {

	@Autowired
	private RestTemplate restTemplate;

	public EmployeeController() {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("{id}")
	public Map<String, Object> getEmployee(final HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			@PathVariable("id") final Long id) {

		Map<String, Object> returnMap = new HashMap<>();

		if (id == 1) {
			returnMap.putAll(Map.of("id", "1", "firstName", "Abc", "lastName", "Efg", "contactNo", "1234567890"));
		} else if (id == 2) {
			returnMap.putAll(Map.of("id", "2", "firstName", "DEF", "lastName", "ABCD", "contactNo", "9876543210"));
		} else if (id == 3) {
			returnMap.putAll(Map.of("id", "3", "firstName", "XYz", "lastName", "Test", "contactNo", "4561237890"));
		}

		// get address from service-2
		final String url = "http://localhost:8180/api/service-2/employee/address/" + id;
		final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		String authHeader="";
		if(httpServletResponse.getHeader("AuthorizationToken") !=  null) {
			authHeader=httpServletResponse.getHeader("AuthorizationToken");
		}
		headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
		headers.add("Authorization", "Bearer "+authHeader);
		final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		final ResponseEntity<Map> response = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
		final Map<String, Object> address = response.getBody();

		returnMap.put("address", address);
		return returnMap;
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
}
