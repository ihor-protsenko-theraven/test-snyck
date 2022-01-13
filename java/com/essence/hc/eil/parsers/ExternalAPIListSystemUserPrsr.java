package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.SystemUser;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIListSystemUserPrsr implements IParser<List<SystemUser>> {
	
	private String response;
	private String responseDescription;
	private boolean value;
	private List<ExternalAPISystemUserWithPrefsPrsr> users;

	@Override
	public List<SystemUser> parse() {
		List<SystemUser> listSystemUsers = new ArrayList <SystemUser>();
		
		try {		
			if(users != null)		 
				for(ExternalAPISystemUserWithPrefsPrsr i: users){
					listSystemUsers.add(i.parse());
				}
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		
		return listSystemUsers;
	}
	
	public List<ExternalAPISystemUserWithPrefsPrsr> getUsers() {
		return users;
	}

	public void setUsers(List<ExternalAPISystemUserWithPrefsPrsr> users) {
		this.users = users;
	}

	
	/* Getters and setters are duplicated because this parser is used for both APIs. 
	 * The response has the same form, but property names are different.
	 */
	
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
