package com.api.gateway.configs;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfiguration implements Filter {

	private static final String ALLOWED_HEADERS = "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Origin, Authorization, AUTH-TOKEN, BranchId";
	private static final String ALLOWED_METHODS = "OPTIONS, GET, POST, DELETE, PUT, PATCH, HEAD";
	private static final String EXPOSED_HEADERS = "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Content-Disposition";
	private static final String ALLOWED_ORIGIN = "*";
	private static final Integer MAX_AGE = 3600;



	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

//		authenticationProcess(httpServletRequest, httpServletResponse);

		httpServletResponse.addHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
		httpServletResponse.addHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
		httpServletResponse.addHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
		httpServletResponse.addHeader("Access-Control-Expose-Headers", EXPOSED_HEADERS);
		httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
		httpServletResponse.addIntHeader("Access-Control-Max-Age", MAX_AGE);
		if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

//	public void authenticationProcess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//
//		final String appName = this.headerAppName;
//
//		if (appName == null || appName.isEmpty()) {
//			System.out.println("Application Name not found");
//			throw new AuthException(AuthException.Codes.EA_001);
//		}
//
//		String newToken = "";
//
//		final String accessToken = AppConfig.getToken(appName);
//		if (null != accessToken && !accessToken.isEmpty()) {
//			System.out.println("Get existing token for --> " + appName);
//			newToken = AppConfig.getToken(appName);
//		} else {
//			System.out.println("Generate new token for --> " + appName);
//			newToken = jwtUtil.getToken(appName);
//		}
//
////		final Long exp = jwtUtil.getExpirationDate(newToken);
////		if (exp != 0L && exp > -1) {
////			final Date now = new Date();
////			if (new Date(exp).before(now)) {
////				System.out.println("Token is expired for  --> " + appName);
////				newToken = jwtUtil.getToken(appName);
////				System.out.println("Generate new token for --> " + appName);
////				// throw new AuthException(AuthException.Codes.EA_003);
////			}
////		}
//
//		System.out.println("********* " + newToken);
//		httpServletResponse.addHeader("AuthorizationToken", newToken);
//	}

}
