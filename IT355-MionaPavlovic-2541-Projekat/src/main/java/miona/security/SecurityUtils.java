package miona.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import miona.data.entities.UserAccount;

public class SecurityUtils {
	
	/**
	 * Method for getting user from security context
	 * @return {@link UserAccount}
	 */
	public static UserAccount getUserFromContext() {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Object details = null;

		try {
			details = securityContext.getAuthentication().getPrincipal();
		} catch (NullPointerException e) {
			details = null;
		}
		UserAccount user = null;
		if (details != null && details instanceof UserAccount) {
			user = (UserAccount) details;
		}
		return user;
	}
	
}
