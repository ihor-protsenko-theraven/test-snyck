package com.essence.hc.model;

import java.util.List;

public class PanelDetails {
	private String simNumber;
	private String landlineNumber;
	private String panelSerialNumber;
	private String serviceProviderSerialNumber;
	private String dtmf;
	private boolean supportDeviceSync;
	private String deviceType;
	private String firmwareVersion;
	private String gsmModuleVersion;
	private List<String> communicationInterfaces;

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
	public String getServiceProviderSerialNumber() {
		return serviceProviderSerialNumber;
	}
	public void setServiceProviderSerialNumber(String serviceProviderSerialNumber) {
		this.serviceProviderSerialNumber = serviceProviderSerialNumber;
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
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getGsmModuleVersion() {
		return gsmModuleVersion;
	}
	public void setGsmModuleVersion(String gsmModuleVersion) {
		this.gsmModuleVersion = gsmModuleVersion;
	}
	public List<String> getCommunicationInterfaces() {
		return communicationInterfaces;
	}
	public void setCommunicationInterfaces(List<String> communicationInterfaces) {
		this.communicationInterfaces = communicationInterfaces;
	}
}
