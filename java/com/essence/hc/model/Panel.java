package com.essence.hc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.essence.hc.model.Vendor.ServiceType;

/**
 * Care@Home Control Panel device
 * 
 * @author oscar.canalejo
 *
 */
public class Panel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String panelId;
	private String account; // Essence Account Number
	private String name;
	private String simNumber;
	private String landlineNumber;
	private String iDTMFCode;
	private String serialNumber; // Essence Serial Number
	private String customerSerialNumber; // Vendor's Serial Number
	private String version;
	private String InstallNotes;
	private ServiceType serviceType;
	private boolean hasPets;
	private String serviceProviderAccountNumber;
	private String timeZone;
	private boolean enableActiveService;
	private PanelSettings panelSettings;
	private boolean allowEmergencyCallModifications;
	private boolean supportActiveService;
	private Date firstStepCountTime;
	private String panelType;
	private List<String> communicationInterfaces;

	/**
	 * The codename used to identify the service package
	 */
	private String servicePackageCode;

	public static enum PanelFeature {
		ACTIVE_SERVICE("activeService");
		private final String featureKey;

		PanelFeature(String featureKey) {
			this.featureKey = featureKey;
		}

		public String getFeatureKey() {
			return this.featureKey;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

	public String getLandlineNumber() {
		return landlineNumber;
	}

	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public String getiDTMFCode() {
		return iDTMFCode;
	}

	// public String getDTMFCodeString() {
	// if (iDTMFCode == 0) {
	// return "1234"; // default value for DTMF code
	// } else {
	// return Integer.toString(iDTMFCode);
	// }
	// }

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCustomerSerialNumber() {
		return customerSerialNumber;
	}

	public void setCustomerSerialNumber(String customerSerialNumber) {
		this.customerSerialNumber = customerSerialNumber;
	}

	public String getInstallNotes() {
		return InstallNotes;
	}

	public void setInstallNotes(String installNotes) {
		InstallNotes = installNotes;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public boolean hasPets() {
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

	public PanelSettings getPanelSettings() {
		return panelSettings;
	}

	public void setPanelSettings(PanelSettings panelSettings) {
		this.panelSettings = panelSettings;
	}

	public void setiDTMFCode(String iDTMFCode) {
		// 1234 default value for DTMF code
		this.iDTMFCode = (iDTMFCode == null || iDTMFCode.isEmpty()) ? "1234" : iDTMFCode;
	}

	public boolean isAllowEmergencyCallModifications() {
		return allowEmergencyCallModifications;
	}

	public void setAllowEmergencyCallModifications(boolean allowEmergencyCallModifications) {
		this.allowEmergencyCallModifications = allowEmergencyCallModifications;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isSupportActiveService() {
		return supportActiveService;
	}

	public void setSupportActiveService(boolean supportActiveService) {
		this.supportActiveService = supportActiveService;
	}

	public Date getFirstStepCountTime() {
		return firstStepCountTime;
	}

	public void setFirstStepCountTime(Date firstStepCountTime) {
		this.firstStepCountTime = firstStepCountTime;
	}

	public String getServicePackageCode() {
		return servicePackageCode;
	}

	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}

	public String getPanelType() {
		return panelType;
	}

	public void setPanelType(String panelType) {
		this.panelType = panelType;
	}

	public List<String> getCommunicationInterfaces() {
		return communicationInterfaces;
	}

	public void setCommunicationInterfaces(List<String> communicationInterfaces) {
		this.communicationInterfaces = communicationInterfaces;
	}

}
