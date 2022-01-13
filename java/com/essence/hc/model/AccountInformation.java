package com.essence.hc.model;

import java.util.List;

public class AccountInformation {

	private AccountDetails accountDetails;
	private PanelDetails panelDetails;
	private List<AccountInformationUser> users;

	// 2.5.7 devices not needed so far

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
