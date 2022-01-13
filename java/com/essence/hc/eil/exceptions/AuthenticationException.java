package com.essence.hc.eil.exceptions;

public class AuthenticationException extends RemoteIntegrationException {
	
	private static final long serialVersionUID = 1L;

	public AuthenticationException(String msgKey) {
		super(msgKey);
	}

}
