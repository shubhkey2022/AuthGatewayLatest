package com.service.errorHandlers;

import java.util.HashMap;
import java.util.Map;

public class AuthException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8965361375160676162L;
	private final String message;
	private final String code;

	public AuthException(final String code) {
		this.message = MESSAGES.get(code);
		this.code = code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public String getCode() {
		return this.code;
	}

	public static class Codes {
		private Codes() {
		}

		public static final String EA_001 = "EA_001";
		public static final String EA_002 = "EA_002";
		public static final String EA_003 = "EA_003";
		public static final String EA_004 = "EA_004";
		public static final String EA_005 = "EA_005";
		public static final String EA_006 = "EA_006";
		public static final String EA_007 = "EA_007";
		public static final String EA_008 = "EA_008";
	}

	private static final Map<String, String> MESSAGES = new HashMap<>();

	static {
		MESSAGES.put(Codes.EA_001, "Missing Application Name");
		MESSAGES.put(Codes.EA_002, "Invalid Jwt token");
		MESSAGES.put(Codes.EA_003, "Jwt is expired");
		MESSAGES.put(Codes.EA_004, "Unauthorized access to API");
		MESSAGES.put(Codes.EA_005, "Invalid Application Name");
	}
}
