package com.essence.hc.eil.parsers;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.model.Installation;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Vendor.ServiceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIAccountPrsr implements IParser<Installation> {
	
	private String installationStatus;
	private String accountNumber;
	private String name;
	private String address;
	private String homePhone;
	private boolean hasPets;
	private String installationNotes;
	private String simNumber;
	private String landlineNumber;
	private int panelId;
	private String serviceType;
	private String panelSerialNumber;
	private String serviceProviderSerialNumber;
	private String dtmfCode;
	private String serviceProviderAccountNumber;
	private List<String> communicationInterfaces;
	private Map<String, Boolean> supportedFeatures;
	

	@Override
	public Installation parse() {
		Installation installation = new Installation();
		Panel panel = new Panel();
		
		panel.setAccount(accountNumber);
		panel.setName(name);
		panel.setHasPets(hasPets);
		panel.setInstallNotes(installationNotes);
		panel.setSimNumber(simNumber);
		panel.setLandlineNumber(landlineNumber);
		panel.setPanelId(new Integer(panelId).toString());
		if ("Express".equals(serviceType)) {
			panel.setServiceType(ServiceType.EXPRESS);
		} else if ("Analytics".equals(serviceType)) {
			panel.setServiceType(ServiceType.ANALYTICS);
		}
		panel.setSerialNumber(panelSerialNumber);
		panel.setCustomerSerialNumber(serviceProviderSerialNumber);
		panel.setiDTMFCode(dtmfCode);
		panel.setServiceProviderAccountNumber(serviceProviderAccountNumber);
		panel.setCommunicationInterfaces(communicationInterfaces);
		if ( supportedFeatures != null && !supportedFeatures.isEmpty() ) {
			if ( supportedFeatures.containsKey("activeService") ) {
				panel.setEnableActiveService(supportedFeatures.get("activeService"));
			}
		}
		installation.setPanel(panel);
		
		return installation;
	}

	public String getInstallationStatus() {
		return installationStatus;
	}

	public void setInstallationStatus(String installationStatus) {
		this.installationStatus = installationStatus;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public boolean isHasPets() {
		return hasPets;
	}

	public void setHasPets(boolean hasPets) {
		this.hasPets = hasPets;
	}

	public String getInstallationNotes() {
		return installationNotes;
	}

	public void setInstallationNotes(String installationNotes) {
		this.installationNotes = installationNotes;
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

	public int getPanelId() {
		return panelId;
	}

	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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

	public String getDtmfCode() {
		return dtmfCode;
	}

	public void setDtmfCode(String dtmfCode) {
		this.dtmfCode = dtmfCode;
	}

	public String getServiceProviderAccountNumber() {
		return serviceProviderAccountNumber;
	}

	public void setServiceProviderAccountNumber(String serviceProviderAccountNumber) {
		this.serviceProviderAccountNumber = serviceProviderAccountNumber;
	}
	
	public List<String> getCommunicationInterfaces() {
		return communicationInterfaces;
	}

	public void setCommunicationInterfaces(List<String> communicationInterfaces) {
		this.communicationInterfaces = communicationInterfaces;
	}

	public Map<String, Boolean> getSupportedFeatures(){
		return this.supportedFeatures;
	}
	
	public void setSupportedFeatures(Map<String, Boolean> supportedFeatures){
		this.supportedFeatures = supportedFeatures;
	}
}
