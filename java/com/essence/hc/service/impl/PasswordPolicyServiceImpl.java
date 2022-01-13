package com.essence.hc.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.essence.hc.eil.exceptions.AuthenticationException;
import com.essence.hc.eil.exceptions.OperationNotAllowedException;
import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.exceptions.IncorrectEulaVersionException;
import com.essence.hc.exceptions.NewPasswordInvalidByHistoryException;
import com.essence.hc.exceptions.NewPasswordInvalidByPolicyException;
import com.essence.hc.exceptions.PasswordInvalidException;
import com.essence.hc.model.PasswordPolicy;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.User;
import com.essence.hc.service.PasswordPolicyService;
import com.essence.security.SecurityService;

@Service
public class PasswordPolicyServiceImpl implements PasswordPolicyService {

	public static final int EULA_RETRIES = 3;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private SecurityService securityService;

	@Autowired
	@Qualifier("requestProcessorExternal")
	private RequestProcessor reqProcessor;

	// private RequestProcessor reqProcessor = new DummyRequestProcessor();

	/**
	 * Anauthenticated request in order to recover the password
	 * 
	 * @param username
	 *            - the user name requesting the password resert
	 */
	public void resetPassword(String username) {

		HashMap<String, String> reqParams = new HashMap<>();
		reqParams.put("userName", username);

		ResponseStatus rs = reqProcessor.performRequest(ResponseStatus.class, "external_reset_password", reqParams);

		if (rs.getNumErr() != 0) {
			throw new RuntimeException(String.format("Unknown exception in the backend: %s", rs.getMessageErr()));
		}

		LOG.debug("Successful response for the resetPassword request - isOk: {}", rs.isOK());

	}

	/**
	 * Tries to change the the current expired password for a user.
	 * 
	 * May throw NewPasswordInvalidByPolicyException on invalid pas
	 */
	public void changeExpiredPassword(String username, String currentPassword, String newPassword) {
		HashMap<String, String> reqParams = new HashMap<>();
		reqParams.put("userName", username);
		reqParams.put("currentPassword", currentPassword);
		reqParams.put("newPassword", newPassword);

		ResponseStatus rs = reqProcessor.performRequest(ResponseStatus.class, "external_change_expired_password",
				reqParams);
		if (rs.getNumErr() == 112) {
			throw new NewPasswordInvalidByPolicyException();
		} else if (rs.getNumErr() == 113) {
			throw new NewPasswordInvalidByHistoryException();
		} else if (rs.getNumErr() == 1) {
			throw new RuntimeException(String.format("Unknown exception in the backend: %s", rs.getMessageErr()));
		} else if ((rs.getNumErr() == 30) || (rs.getNumErr() == 2)) {
			throw new PasswordInvalidException("One of the values of the passwords might be incorrect");
		} else if (rs.getNumErr() != 0) {
			throw new RuntimeException();
		}

		LOG.debug("Successful response for the changeExpiredPassword request - isOk: {}", rs.isOK());
	}

	public void changeMyPassword(String currentPassword, String newPassword) {
		HashMap<String, String> reqHeaders = createTokenHeader();
		HashMap<String, String> reqParams = new HashMap<>();
		reqParams.put("currentPassword", currentPassword);
		reqParams.put("newPassword", newPassword);
		// ResponseStatus rs = new
		// DummyRequestProcessor().performRequest(ResponseStatus.class,
		// "external_change_password", reqParams,
		ResponseStatus rs = reqProcessor.performRequest(ResponseStatus.class, "external_change_password", reqParams,
				reqHeaders);
		if (rs.getNumErr() == 112) {
			throw new NewPasswordInvalidByPolicyException();
		} else if (rs.getNumErr() == 113) {
			throw new NewPasswordInvalidByHistoryException();
		} else if ((rs.getNumErr() == 30) || (rs.getNumErr() == 2)) {
			throw new PasswordInvalidException("One of the values of the passwords might be incorrect");
		} else if (rs.getNumErr() == 6) {
			throw new OperationNotAllowedException("No user id - Username does not belong to any active user");
		} else if (rs.getNumErr() == 123) {
			throw new AuthenticationException("Invalid Token");
		} else if (rs.getNumErr() == 1) {
			throw new RuntimeException(String.format("Unknown exception in the backend: %s", rs.getMessageErr()));
		} else if (rs.getNumErr() != 0) {
			throw new RuntimeException();
		}
	}

	public void changeUserPassword(String username, String newPassword) {
		HashMap<String, String> reqHeaders = createTokenHeader();
		HashMap<String, String> reqParams = new HashMap<>();
		reqParams.put("newPassword", newPassword);
		reqParams.put("userName", username);
		ResponseStatus rs = reqProcessor.performRequest(ResponseStatus.class, "external_change_user_password",
				reqParams, reqHeaders);

		if (rs.getNumErr() == 112) {
			throw new NewPasswordInvalidByPolicyException();
		} else if (rs.getNumErr() == 113) {
			throw new NewPasswordInvalidByHistoryException();
		} else if (rs.getNumErr() == 30) {
			throw new OperationNotAllowedException("The token does not belong to a user admin");
		} else if (rs.getNumErr() == 123) {
			throw new AuthenticationException("Invalid Token");
		} else if (rs.getNumErr() == 6) {
			throw new OperationNotAllowedException("No user id - Username does not belong to any active user");
		} else if (rs.getNumErr() == 1) {
			throw new RuntimeException(String.format("Unknown exception in the backend: %s", rs.getMessageErr()));
		} else if (rs.getNumErr() != 0) {
			throw new RuntimeException(String.format("Unknown exception in the backend: %s - Maybe it is not present in the HLD", rs.getMessageErr()));
		}
	}

	@Override
	public PasswordPolicy getPasswordPolicy() {

		HashMap<String, String> reqHeaders = createTokenHeader();
		HashMap<String, String> reqParams = new HashMap<String, String>();
		// reqParams.put("sUsrType", String.valueOf(userType.getId()));
		PasswordPolicy pwdPolicy = reqProcessor.performRequest(PasswordPolicy.class, "external_password_policy",
				reqParams, reqHeaders);
		LOG.debug("Password policy return from backend: {}", pwdPolicy);
		return pwdPolicy;
	}

	public void acceptEula(String version) {
		HashMap<String, String> reqHeaders = createTokenHeader();
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("version", version);
		int counter = 0;
		
		ResponseStatus rs = null;
		
		// Eula Accept has to be retried 3 times
		boolean breakDoWhile = false;
		do {
			rs = reqProcessor.performRequest(ResponseStatus.class, "external_accept_eula", reqParams,
					reqHeaders);
			if (rs.getNumErr() == 0)
				return;
			if (rs.getNumErr() == 154) {
				breakDoWhile = true;
			}
		} while (counter++< EULA_RETRIES && !breakDoWhile);
		
		if (rs.getNumErr() == 1) {
			throw new RuntimeException("Unknown error in the backend");
		} else if (rs.getNumErr() == 123) {
			throw new AuthenticationException("Invalid Token");
		} else if (rs.getNumErr() == 154) {
			throw new IncorrectEulaVersionException(rs.getMessageErr());
		}
	}

	private HashMap<String, String> createTokenHeader() {
		User currentUser = securityService.getPrincipal();
		if (currentUser != null) {
			String token = currentUser.getToken();
			if (StringUtils.hasText(token)) {
				return createTokenHeader(token);
			}
		}
		return null;
	}

	private HashMap<String, String> createTokenHeader(String token) {
		HashMap<String, String> reqHeaders = new HashMap<String, String>();
		reqHeaders.put("Authorization", "Token " + token);
		return reqHeaders;
	}

}
