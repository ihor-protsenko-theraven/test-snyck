package com.essence.hc.model;

public class ThirdPartyUser extends User {

	private static final long serialVersionUID = 1L;
	
	private String logoutRedirectUrl;
	private String errorRedirectUrl;
	private String externalPartyAuthenticationProviderType;
	
	public String getLogoutRedirectUrl() {
		return logoutRedirectUrl;
	}
	public void setLogoutRedirectUrl(String logoutRedirectUrl) {
		this.logoutRedirectUrl = logoutRedirectUrl;
	}
	public String getErrorRedirectUrl() {
		return errorRedirectUrl;
	}
	public void setErrorRedirectUrl(String errorRedirectUrl) {
		this.errorRedirectUrl = errorRedirectUrl;
	}
	
	public String getExternalPartyAuthenticationProviderType() {
		return externalPartyAuthenticationProviderType;
	}
	public void setExternalPartyAuthenticationProviderType(String externalPartyAuthenticationProviderType) {
		this.externalPartyAuthenticationProviderType = externalPartyAuthenticationProviderType;
	}
	
}
