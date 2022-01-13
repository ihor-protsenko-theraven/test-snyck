package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.essence.hc.model.Panel;
import com.essence.hc.model.Panel.PanelFeature;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ExternalAPIPanelPrsr implements IGenerator<Panel> {

	private String account;
	// TODO in order to keep coherence between other API methods this attribute should be changed to serviceProviderAccountNumber
	private String serviceProviderAccount;
	private String panelSerialNumber;
	private String serviceProviderSerialNumber;
	private String simNumber;
	private String landlineNumber;
	private String dtmf;
	private String hasPets;
	private String timeZone;
	private String notes;
	private Map<String, Boolean> supportedFeatures;
	private String servicePackageCode;
	private ActiveEmergencyCallAccountSettingsPrsr settings;
	private List<String> communicationInterfaces;
	
	@Override
	public void load(Panel panel){
		
		this.setAccount(panel.getAccount());
		this.setServiceProviderAccount(panel.getServiceProviderAccountNumber());
		this.setPanelSerialNumber(panel.getSerialNumber());
		this.setServiceProviderSerialNumber(panel.getCustomerSerialNumber());
		this.setSimNumber(panel.getSimNumber());
		this.setLandlineNumber(panel.getLandlineNumber());
		this.setDtmf(panel.getiDTMFCode());
		this.setHasPets(String.valueOf(panel.hasPets()));
		this.setTimeZone(panel.getTimeZone());
		this.setNotes(panel.getInstallNotes());
		this.setServicePackageCode(panel.getServicePackageCode());
		this.setCommunicationInterfaces(panel.getCommunicationInterfaces());
		
		Map<String, Boolean> supportedFeatures =  new HashMap<String, Boolean>();
		supportedFeatures.put(PanelFeature.ACTIVE_SERVICE.getFeatureKey(), panel.isEnableActiveService());
		this.setSupportedFeatures(supportedFeatures);
		
		List<String> allowedRoles = new ArrayList(Arrays.asList(panel.getPanelSettings().getActiveEmergencyRolesAllowed()));
		
		ActiveEmergencyCallAccountPrsr activeEmergencyCallAccountPrsr = new ActiveEmergencyCallAccountPrsr();
		activeEmergencyCallAccountPrsr.setAllowChangesBy(allowedRoles);
		
		this.settings = new ActiveEmergencyCallAccountSettingsPrsr();
		this.settings.setActiveEmergencyCall(activeEmergencyCallAccountPrsr);
	
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getServiceProviderAccount() {
		return serviceProviderAccount;
	}
	public void setServiceProviderAccount(String serviceProviderAccount) {
		this.serviceProviderAccount = serviceProviderAccount;
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
	public String getDtmf() {
		return dtmf;
	}
	public void setDtmf(String dtmf) {
		this.dtmf = dtmf;
	}
	public String getHasPets() {
		return hasPets;
	}
	public void setHasPets(String hasPets) {
		this.hasPets = hasPets;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Map<String, Boolean> getSupportedFeatures() {
		return supportedFeatures;
	}

	public void setSupportedFeatures(Map<String, Boolean> supportedFeatures) {
		this.supportedFeatures = supportedFeatures;
	}

	public ActiveEmergencyCallAccountSettingsPrsr getSettings() {
		return settings;
	}

	public void setSettings(ActiveEmergencyCallAccountSettingsPrsr settings) {
		this.settings = settings;
	}

	public String getServicePackageCode() {
		return servicePackageCode;
	}

	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}

	public List<String> getCommunicationInterfaces() {
		return communicationInterfaces;
	}

	public void setCommunicationInterfaces(List<String> communicationInterfaces) {
		this.communicationInterfaces = communicationInterfaces;
	}
	
	
	
}
