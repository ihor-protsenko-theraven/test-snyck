package com.essence.hc.model;

import java.util.List;

import com.essence.hc.model.Vendor.ServiceType;

public class FreePanel {
	
	private String account;
	private String simNumber;
	private String landlineNumber;
	private String panelSerialNumber;
	private String dtmf;
	private boolean supportDeviceSync;
	private String version;
	private String serviceProviderSerialNumber;
	private String deviceType;
	private List<String> communicationInterfaces;

	/**
	 * FreePanels are obtained through a GetFreePanels request. The request accepts a filter for a given servicePackage.
	 * We fullfill the request response with the servicePackage so client side can be aware of it. 
	 */
	private ServiceType potentialServiceType;
	
	
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
	public String getPanelSerialNumber() {
		return panelSerialNumber;
	}
	public void setPanelSerialNumber(String panelSerialNumber) {
		this.panelSerialNumber = panelSerialNumber;
	}
	public String getDtmf() {
		return dtmf;
	}
	public void setDtmf(String dtmf) {
		this.dtmf = dtmf;
	}
	public boolean isSupportDeviceSync() {
		return supportDeviceSync;
	}
	public void setSupportDeviceSync(boolean supportDeviceSync) {
		this.supportDeviceSync = supportDeviceSync;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getServiceProviderSerialNumber() {
		return serviceProviderSerialNumber;
	}
	public void setServiceProviderSerialNumber(String serviceProviderSerialNumber) {
		this.serviceProviderSerialNumber = serviceProviderSerialNumber;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public List<String> getCommunicationInterfaces() {
		return communicationInterfaces;
	}
	public void setCommunicationInterfaces(List<String> communicationInterfaces) {
		this.communicationInterfaces = communicationInterfaces;
	}
	public ServiceType getPotentialServiceType() {
		return potentialServiceType;
	}
	public void setPotentialServiceType(ServiceType potentialServiceType) {
		this.potentialServiceType = potentialServiceType;
	}
}
