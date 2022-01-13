package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.VendorConfigurationSettings;
import com.essence.hc.model.VendorConfigurationUserSettings;
import com.essence.hc.model.VendorConfigurationTelehealth;
import com.essence.hc.model.ServicePackageInformation;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.Vendor.ServiceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorConfigurationSettingsPrsr implements IParser<VendorConfigurationSettings> {
	
	private String response;
	private String responseDescription;
	private boolean value;
	private String message;
	
	private String vendorId;
	private boolean smsEnabled;
	private VendorConfigurationTelehealth teleHealth;
	
	private List<VendorConfigurationUserSettingsPrsr> userSettings;
	private List<ServicePackageInformation> servicePackages;

	@Override
	public VendorConfigurationSettings parse() {
		
		VendorConfigurationSettings vcs = new VendorConfigurationSettings();
		
		vcs.setVendorId(this.vendorId);
		vcs.setSmsEnabled(this.smsEnabled);
		vcs.setTelehealth(this.teleHealth);
		
		if(userSettings != null) {
			final List<VendorConfigurationUserSettings> us = new ArrayList<VendorConfigurationUserSettings>();
			for (VendorConfigurationUserSettingsPrsr vcus : userSettings) {
				us.add(vcus.parse());
			}
			vcs.setUserSettings(us);
		}
		
		vcs.setServicePackages(this.servicePackages);
		
		return vcs;
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

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public boolean isSmsEnabled() {
		return smsEnabled;
	}

	public void setSmsEnabled(boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}
	
	public VendorConfigurationTelehealth getTeleHealth() {
		return teleHealth;
	}

	public void setTeleHealth(VendorConfigurationTelehealth teleHealth2) {
		this.teleHealth = teleHealth2;
	}

	public List<VendorConfigurationUserSettingsPrsr> getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(List<VendorConfigurationUserSettingsPrsr> userSettings) {
		this.userSettings = userSettings;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ServicePackageInformation> getServicePackages() {
		return servicePackages;
	}

	public void setServicePackages(List<ServicePackageInformation> servicePackages) {
		this.servicePackages = servicePackages;
	}
}
