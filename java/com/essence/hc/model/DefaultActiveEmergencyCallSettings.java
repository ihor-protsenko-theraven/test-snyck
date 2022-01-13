package com.essence.hc.model;

import java.util.List;

public class DefaultActiveEmergencyCallSettings {
	
	private boolean enableCall;
	private String phoneNumber;
	private List<String> allowChangesBy;
	
	public boolean isEnableCall() {
		return enableCall;
	}
	public void setEnableCall(boolean enableCall) {
		this.enableCall = enableCall;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<String> getAllowChangesBy() {
		return allowChangesBy;
	}
	public void setAllowChangesBy(List<String> allowChangesBy) {
		this.allowChangesBy = allowChangesBy;
	}
	
}
