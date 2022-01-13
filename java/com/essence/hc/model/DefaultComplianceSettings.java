package com.essence.hc.model;

public class DefaultComplianceSettings {

	private int defaultComplianceNotificationInHours;
	private int complianceEventMinimumLevel;
	
	public int getDefaultComplianceNotificationInHours() {
		return defaultComplianceNotificationInHours;
	}
	public int getComplianceEventMinimumLevel() {
		return complianceEventMinimumLevel;
	}
	public void setDefaultComplianceNotificationInHours(int defaultComplianceNotificationInHours) {
		this.defaultComplianceNotificationInHours = defaultComplianceNotificationInHours;
	}
	public void setComplianceEventMinimumLevel(int complianceEventMinimumLevel) {
		this.complianceEventMinimumLevel = complianceEventMinimumLevel;
	}

}
