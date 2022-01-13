package com.essence.hc.model;

public class ExternalAPILogin {
	
	private PasswordExpirationData passwordExpirationData;
	private PasswordPolicy passwordPolicy;
	private PasswordExpirationReason passwordExpirationReason;
	
	private Eula eula;
	
	private String response;
	private String responseDescription;
	private boolean value;
	private String token;
	
	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return the responseDescription
	 */
	public String getResponseDescription() {
		return responseDescription;
	}
	/**
	 * @param responseDescription the responseDescription to set
	 */
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	/**
	 * @return the value
	 */
	public boolean isValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	public PasswordExpirationData getPasswordExpirationData() {
		return passwordExpirationData;
	}
	public void setPasswordExpirationData(PasswordExpirationData passwordExpirationData) {
		this.passwordExpirationData = passwordExpirationData;
	}
	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}
	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}
	public PasswordExpirationReason getPasswordExpirationReason() {
		return passwordExpirationReason;
	}
	public void setPasswordExpirationReason(PasswordExpirationReason passwordExpirationReason) {
		this.passwordExpirationReason = passwordExpirationReason;
	}
	public Eula getEula() {
		return eula;
	}
	public void setEula(Eula eula) {
		this.eula = eula;
	}

}
