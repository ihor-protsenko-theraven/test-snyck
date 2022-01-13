package com.essence.hc.model;

public class AccountDetails {
	private String account;
	private String activationStatus;
	private boolean hasPets;
	private String serviceProviderAccountNumber;
	private String servicePackageCode;
	private String timeZone;
	private boolean enableActiveService;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getActivationStatus() {
		return activationStatus;
	}
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	public boolean isHasPets() {
		return hasPets;
	}
	public void setHasPets(boolean hasPets) {
		this.hasPets = hasPets;
	}
	public String getServiceProviderAccountNumber() {
		return serviceProviderAccountNumber;
	}
	public void setServiceProviderAccountNumber(String serviceProviderAccountNumber) {
		this.serviceProviderAccountNumber = serviceProviderAccountNumber;
	}
	public String getServicePackageCode() {
		return servicePackageCode;
	}
	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public boolean isEnableActiveService() {
		return enableActiveService;
	}
	public void setEnableActiveService(boolean enableActiveService) {
		this.enableActiveService = enableActiveService;
	}
}
