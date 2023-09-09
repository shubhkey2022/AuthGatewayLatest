package com.api.gateway.constants;

public class Constants {

	public static final String SECURE_PATH = "s";

	public static final String AUTHENTICATED_USER_ID_ATTR_KEY = "currentUserId";

	public static final String ROLES = "User.Read.All";
	public static final String ROLES1 = "Group.Read.All";

	public static final String[] APPLCIATION_DISPLAY_NAMES = { "SMBEmployeeServices_UAT_9908",
			"SMBMerchantServices_UAT_9908", "SMBDomainServices_UAT_9908" };

	public static class JwtKeys {
		public static final String EXPIRATION = "exp";
		public static final String APP_DISPLAY_NAME = "app_displayname";
		public static final String ROLES = "roles";
	}
}
