package com.api.gateway.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.gateway.constants.AppEntityCodes;
import com.api.gateway.errorHandlers.AuthException;
import com.api.gateway.services.JwtTokenUtil;


@RestController
@RequestMapping(AppEntityCodes.GATEWAY)
public class GatewayController {
	
	Logger logger=LoggerFactory.getLogger(GatewayController.class);
	
	@Value("${header.auth.name}")
	private String authHeaderName;
	
	@Autowired
	private JwtTokenUtil jwtUtil;
	
	@GetMapping("/validateToken")
	public Map<String, Object> validateToken(final HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		
		String jwtToken = "";
		String appName = "";
		Boolean result=true;
		String error="";
		Map<String, Object> claims=null;
		final String requestTokenHeader = httpServletRequest.getHeader(this.authHeaderName);
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				final Long exp = jwtUtil.getExpirationDate(jwtToken);
				appName = jwtUtil.getappNameFromToken(jwtToken);
				if (exp != 0L && exp > -1) {
					final Date now = new Date();
					if (new Date(exp).before(now)) {
						System.out.println("Token is expired for  --> ");
						result=false;
						error=AuthException.getMessage(AuthException.Codes.EA_003);
						//throw new AuthException(AuthException.Codes.EA_003);
					}
				}

//				if (!jwtUtil.validateRole(jwtToken)) {
//					error=AuthException.getMessage(AuthException.Codes.EA_004);
//					//throw new AuthException(AuthException.Codes.EA_004);
//				}

				if (!jwtUtil.validateDisplayName(jwtToken, appName)) {
					result=false;
					error=AuthException.getMessage(AuthException.Codes.EA_004);
					//throw new AuthException(AuthException.Codes.EA_004);
				}
				
				claims=jwtUtil.getClaim(jwtToken);
				System.out.println(claims);
			} catch (IllegalArgumentException e) {
				result=false;
				error=AuthException.getMessage(AuthException.Codes.EA_006);
				System.out.println("Unable to get JWT Token");
			}
		} else {
			result=false;
			logger.warn("JWT Token does not begin with Bearer String");
			error = AuthException.getMessage(AuthException.Codes.EA_007);
			//throw new AuthException(AuthException.Codes.EA_004);
		}


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("error", error);
		map.put("claims", claims);
		return map;
	}

}
