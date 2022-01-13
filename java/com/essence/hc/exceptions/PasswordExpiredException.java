package com.essence.hc.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.essence.hc.model.PasswordExpirationReason;
import com.essence.hc.model.PasswordPolicy;

public class PasswordExpiredException extends AuthenticationException {
	
	private final PasswordExpirationReason reason;
	private final PasswordPolicy policy;

	public PasswordExpiredException(PasswordExpirationReason reason, PasswordPolicy passwordPolicy) {
		super("Password Expired Exception");
		this.reason = reason;
		this.policy = passwordPolicy;
	}

	public PasswordExpirationReason getReason() {
		return reason;
	}

	public PasswordPolicy getPolicy() {
		return policy;
	}

}
