package com.essence.hc.eil.parsers;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.SystemUser;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPILoggedInSystemUserPrsr implements IParser<SystemUser> {

	private ExternalAPISystemUserPrsr user;
	private String response;
	private String responseDescription;
	private boolean value;
	
	@Override
	public SystemUser parse() {
		return user.parse();
	}
	
	
	public ExternalAPISystemUserPrsr getUser() {
		return user;
	}


	public void setUser(ExternalAPISystemUserPrsr user) {
		this.user = user;
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

	@JsonProperty("Value")
	public boolean isValue() {
		return value;
	}

	@JsonProperty("Value")
	public void setValue(boolean value) {
		this.value = value;
	}
	
	
}
