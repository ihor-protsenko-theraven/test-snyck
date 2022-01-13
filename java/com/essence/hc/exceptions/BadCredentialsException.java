package com.essence.hc.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.essence.hc.model.PasswordExpirationReason;
import com.essence.hc.model.PasswordPolicy;

public class BadCredentialsException extends AuthenticationException {
	

	public BadCredentialsException() {super("Bad credentials");}


}
