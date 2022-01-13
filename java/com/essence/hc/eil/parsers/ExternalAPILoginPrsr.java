package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.StringUtils;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Eula;
import com.essence.hc.model.ExternalAPILogin;
import com.essence.hc.model.PasswordExpirationData;
import com.essence.hc.model.PasswordExpirationReason;
import com.essence.hc.model.PasswordPolicy;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPILoginPrsr implements IParser<ExternalAPILogin> {
	
	private String response;
	private String responseDescription;
	private boolean value;
	private String token;
	
	private PasswordExpirationData passwordExpirationData;
	private PasswordPolicy passwordPolicy;
	private String passwordExpirationReason;
	
	private Eula eula;
	
	public ExternalAPILoginPrsr() {
	}


	@Override
	public ExternalAPILogin parse() {

		ExternalAPILogin login = new ExternalAPILogin();
		
		try{
			login.setResponse(response);
			login.setResponseDescription(responseDescription);
			login.setValue(value);
			login.setToken(token);
			login.setPasswordExpirationData(this.passwordExpirationData);
			login.setPasswordPolicy(this.passwordPolicy);
			login.setEula(eula);
			if (StringUtils.hasText(this.passwordExpirationReason)) {
				login.setPasswordExpirationReason(PasswordExpirationReason.valueOf(this.passwordExpirationReason.toUpperCase()));
			} else {
				login.setPasswordExpirationReason(PasswordExpirationReason.NONE);
			}
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return login;
	}


	/**
	 * @return the response
	 */
	@JsonProperty("Response")
	public String getResponse() {
		return response;
	}


	/**
	 * @param response the response to set
	 */
	@JsonProperty("Response")
	public void setResponse(String response) {
		this.response = response;
	}


	/**
	 * @return the responseDescription
	 */
	@JsonProperty("ResponseDescription")
	public String getResponseDescription() {
		return responseDescription;
	}


	/**
	 * @param responseDescription the responseDescription to set
	 */
	@JsonProperty("ResponseDescription")
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}


	/**
	 * @return the value
	 */
	@JsonProperty("Value")
	public boolean isValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	@JsonProperty("Value")
	public void setValue(boolean value) {
		this.value = value;
	}


	/**
	 * @return the token
	 */
	@JsonProperty("token")
	public String getToken() {
		return token;
	}


	/**
	 * @param token the token to set
	 */
	@JsonProperty("token")
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


	public String getPasswordExpirationReason() {
		return passwordExpirationReason;
	}


	public void setPasswordExpirationReason(String passwordExpirationReason) {
		this.passwordExpirationReason = passwordExpirationReason;
	}


	public Eula getEula() {
		return eula;
	}


	public void setEula(Eula eula) {
		this.eula = eula;
	}
}
