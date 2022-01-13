package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.PasswordPolicy;

public class PasswordPolicyPrsr implements IParser<PasswordPolicy> {
	
	private String response;
	private String responseDescription;
	private boolean value;
	private String message;
	
	private PasswordPolicy passwordPolicy;

	@Override
	public PasswordPolicy parse() {
		
		return this.passwordPolicy;
	}

	@JsonProperty("Response")
	public String getResponse() {
		return response;
	}

	@JsonProperty("Response")
	public void setResponse(String response) {
		this.response = response;
	}

	@JsonProperty("ResponseDescription")
	public String getResponseDescription() {
		return responseDescription;
	}

	@JsonProperty("ResponseDescription")
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("policy")
	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	@JsonProperty("policy")
	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}
}
