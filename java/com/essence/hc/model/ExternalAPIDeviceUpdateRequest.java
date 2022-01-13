package com.essence.hc.model;

import java.util.List;
import java.util.Map;

public class ExternalAPIDeviceUpdateRequest {

	private String account;
	private String deviceIdentifier;
	private String label;
	private List<Map<String, String>> associations;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getDeviceIdentifier() {
		return deviceIdentifier;
	}
	public void setDeviceIdentifier(String deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Map<String, String>> getAssociations() {
		return associations;
	}
	public void setAssociations(List<Map<String, String>> associations) {
		this.associations = associations;
	}
	
}
