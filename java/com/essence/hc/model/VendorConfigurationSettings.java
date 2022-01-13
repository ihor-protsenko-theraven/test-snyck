package com.essence.hc.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.essence.hc.model.PanelSettings.ActiveRolesTypeAllowed;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.model.VendorConfigurationTelehealth;

public class VendorConfigurationSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	private String vendorId;
	private boolean smsEnabled;

	private List<VendorConfigurationUserSettings> userSettings;
	private List<ServicePackageInformation> servicePackages;
	private VendorConfigurationTelehealth teleHealth;

	public Map<CaregiverType, Integer> getUserLimitationMapByServiceType(ServiceType serviceType) {
		if (userSettings == null) {
			return null;
		}

		for (VendorConfigurationUserSettings vcus : userSettings) {
			if (vcus.getServiceType() == serviceType) {
				return vcus.getUserLimitation();
			}
		}

		return null;
	}

	public int getMaxCaregiversByServiceTypeAndCaregiverType(ServiceType serviceType, CaregiverType caregiverType) {

		return this.getUserLimitationMapByServiceType(serviceType).get(caregiverType);
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
	
	public boolean isTelehealthEnabled() {
		if (teleHealth == null) {
			return false;
		}
		return teleHealth.isEnabled();
	}
	
	public void setTelehealth(VendorConfigurationTelehealth teleHealth2) {
		this.teleHealth = teleHealth2;
	}

	public List<VendorConfigurationUserSettings> getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(List<VendorConfigurationUserSettings> userSettings) {
		this.userSettings = userSettings;
	}

	public List<ServicePackageInformation> getServicePackages() {
		return servicePackages;
	}

	public void setServicePackages(List<ServicePackageInformation> servicePackages) {
		this.servicePackages = servicePackages;
	}

	private boolean isDefaultActiveAllowResidentOverrideByServiceType(ServiceType serviceType) {
		
		if (this.servicePackages == null) {
			return false;
			
		}

		for (ServicePackageInformation spi : this.servicePackages) {
			if (spi.getCodeName().toUpperCase().equals(serviceType.name())) {
				List<String> allowChangesBy = spi.getDefaultActiveEmergencyCallSettings().getAllowChangesBy();
				if (allowChangesBy.contains(ActiveRolesTypeAllowed.ADMINISTRATOR.getActiveRoleTypeAllowed()) && allowChangesBy.contains(ActiveRolesTypeAllowed.CAREGIVER.getActiveRoleTypeAllowed())
						&& allowChangesBy.contains(ActiveRolesTypeAllowed.RESIDENT.getActiveRoleTypeAllowed())) {
					return true;
				}
			}
		}

		return false;

	}
	
	public boolean isDefaultActiveAllowResidentOverrideByExpressServiceType() {
		return isDefaultActiveAllowResidentOverrideByServiceType(ServiceType.EXPRESS);
	}
	
	public boolean isDefaultActiveAllowResidentOverrideByAnalyticsServiceType() {
		return isDefaultActiveAllowResidentOverrideByServiceType(ServiceType.ANALYTICS);
	}

	private boolean isDefaultOutgoingCallByServiceType(ServiceType serviceType) {
		
		if (servicePackages == null) {
			return false;
			
		}

		for (ServicePackageInformation spi : this.servicePackages) {
			if (spi.getCodeName().toUpperCase().equals(serviceType.name())) {
				return spi.getDefaultActiveEmergencyCallSettings().isEnableCall();
			}
		}

		return false;
	}
	public boolean isDefaultOutgoingCallByExpressServiceType() {

		return this.isDefaultOutgoingCallByServiceType(ServiceType.EXPRESS);
	}
	
	public boolean isDefaultOutgoingCallByAnalyticsServiceType() {

		return this.isDefaultOutgoingCallByServiceType(ServiceType.ANALYTICS);
	}
}
