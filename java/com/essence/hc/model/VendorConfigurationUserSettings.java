package com.essence.hc.model;

import java.util.Map;

import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.Vendor.ServiceType;

public class VendorConfigurationUserSettings {
	
	private ServiceType serviceType;
	private Map<CaregiverType, Integer> userLimitation;
	
	public ServiceType getServiceType() {
		return serviceType;
	}
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	public Map<CaregiverType, Integer> getUserLimitation() {
		return userLimitation;
	}
	public void setUserLimitation(Map<CaregiverType, Integer> userLimitation) {
		this.userLimitation = userLimitation;
	}
	
}