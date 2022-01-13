package com.essence.hc.eil.parsers;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.AccountDetails;
import com.essence.hc.model.AccountInformation;
import com.essence.hc.model.AccountInformationUser;
import com.essence.hc.model.PanelDetails;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInformationPrsr implements IParser<AccountInformation> {
	
	private String response;
	private String responseDescription;
	private boolean value;
	private AccountDetails accountDetails;
	private PanelDetails panelDetails;
	private List<AccountInformationUser> users;

	@Override
	public AccountInformation parse() {
		AccountInformation accountInformation = new AccountInformation();
		accountInformation.setAccountDetails(accountDetails);
		accountInformation.setPanelDetails(panelDetails);
		accountInformation.setUsers(users);
		return accountInformation;
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

	public AccountDetails getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(AccountDetails accountDetails) {
		this.accountDetails = accountDetails;
	}

	public PanelDetails getPanelDetails() {
		return panelDetails;
	}

	public void setPanelDetails(PanelDetails panelDetails) {
		this.panelDetails = panelDetails;
	}

	public List<AccountInformationUser> getUsers() {
		return users;
	}

	public void setUsers(List<AccountInformationUser> users) {
		this.users = users;
	}
}
