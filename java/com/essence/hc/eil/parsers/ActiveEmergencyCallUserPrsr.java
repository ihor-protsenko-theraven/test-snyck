package com.essence.hc.eil.parsers;

public class ActiveEmergencyCallUserPrsr {

	private boolean enableCall;
	private String phoneNumberType;
	private String customPhoneNumber;
	private boolean allowModifications;
	
	public boolean isEnableCall() {
		return enableCall;
	}
	public void setEnableCall(boolean enableCall) {
		this.enableCall = enableCall;
	}
	public String getPhoneNumberType() {
		return phoneNumberType;
	}
	public void setPhoneNumberType(String phoneNumberType) {
		this.phoneNumberType = phoneNumberType;
	}
	public String getCustomPhoneNumber() {
		return customPhoneNumber;
	}
	public void setCustomPhoneNumber(String customPhoneNumber) {
		this.customPhoneNumber = customPhoneNumber;
	}
	public boolean isAllowModifications() {
		return allowModifications;
	}
	public void setAllowModifications(boolean allowModifications) {
		this.allowModifications = allowModifications;
	}
	
}
