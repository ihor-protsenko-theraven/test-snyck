package com.essence.hc.eil.parsers;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.util.Util;

/**
 * 
 * Response Parameters;
 * 
 * public int CpId { get; set; } public string Account { get; set; } // Essence
 * Account Number public string Name { get; set; } public string SimNum { get;
 * set; } public string DTMFCode { get; set; } public string SerialNum { get;
 * set; } // Essence Serial Number public string SerialNumCustomer { get; set; }
 * // Vendor's Serial Number public string Version { get; set; } public string
 * InstallNotes { get; set; }
 * 
 * @author enrique.arias
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanelPrsr implements IParser<Panel> {

	private int cpId;
	private String account; // Essence Account Number
	private String name;
	private String simNum;
	private String landlineNumber;
	private String sDTMFCode;
	private String serialNum; // Essence Serial Number
	private String serialNumCustomer; // Vendor's Serial Number
	private String version;
	private String installNotes;
	private String serviceType;
	private boolean hasPets;
	private String timeZone;
	private boolean enableActiveService;
	private boolean supportActiveService;
	private Date firstStepCountTime;
	private boolean allowEmergencyCallModifications;
	private String servicePackageCode;
	private String deviceType;
	private List<String> communicationInterfaces;

	@Override
	public Panel parse() {

		Panel panel = new Panel();

		try {
			panel.setPanelId(String.valueOf(this.getCpId()));
			panel.setSimNumber(String.valueOf(this.getSimNum()));
			panel.setLandlineNumber(String.valueOf(this.getLandlineNumber()));
			panel.setSerialNumber(String.valueOf(this.getSerialNumber()));
			panel.setiDTMFCode(this.getsDTMFCode());
			panel.setAccount(this.getAccount());
			panel.setName(this.name);
			panel.setVersion(String.valueOf(this.getVersion()));
			panel.setCustomerSerialNumber(serialNumCustomer);
			panel.setInstallNotes(installNotes);
			panel.setServiceType(ServiceType.fromString(serviceType));
			panel.setHasPets(this.getHasPets());
			panel.setTimeZone(this.getTimeZone());
			panel.setEnableActiveService(this.getEnableActiveService());
			panel.setAllowEmergencyCallModifications(this.allowEmergencyCallModifications);
			panel.setFirstStepCountTime(this.firstStepCountTime);
			panel.setSupportActiveService(this.supportActiveService);
			panel.setServicePackageCode(this.getServicePackageCode());
			panel.setPanelType(this.getDeviceType());
			panel.setCommunicationInterfaces(this.getCommunicationInterfaces());

		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}
		return panel;
	}

	@JsonProperty("CpId")
	public int getCpId() {
		return cpId;
	}

	@JsonProperty("CpId")
	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	@JsonProperty("Account")
	public String getAccount() {
		return account;
	}

	@JsonProperty("Account")
	public void setAccount(String account) {
		this.account = account;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("SimNum")
	public String getSimNum() {
		return simNum;
	}

	@JsonProperty("SimNum")
	public void setSimNum(String simNum) {
		this.simNum = simNum;
	}

	@JsonProperty("landlineNumber")
	public String getLandlineNumber() {
		return landlineNumber;
	}

	@JsonProperty("landlineNumber")
	public void setLandlineNumber(String landlineNumber) {
		this.landlineNumber = landlineNumber;
	}

	@JsonProperty("DTMFCode")
	public String getsDTMFCode() {
		return sDTMFCode;
	}

	@JsonProperty("DTMFCode")
	public void setsDTMFCode(String sDTMFCode) {
		this.sDTMFCode = sDTMFCode;
	}

	@JsonProperty("SerialNum")
	public String getSerialNumber() {
		return serialNum;
	}

	@JsonProperty("SerialNum")
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	@JsonProperty("Version")
	public String getVersion() {
		return version;
	}

	@JsonProperty("Version")
	public void setVersion(String version) {
		this.version = version;
	}

	@JsonProperty("SerialNumCustomer")
	public String getSerialNumCustomer() {
		return serialNumCustomer;
	}

	@JsonProperty("SerialNumCustomer")
	public void setSerialNumCustomer(String serialNumCustomer) {
		this.serialNumCustomer = serialNumCustomer;
	}

	@JsonProperty("InstallNotes")
	public String getInstallNotes() {
		return installNotes;
	}

	@JsonProperty("InstallNotes")
	public void setInstallNotes(String installNotes) {
		this.installNotes = installNotes;
	}

	@JsonProperty("ServiceType")
	public String getServiceType() {
		return serviceType;
	}

	@JsonProperty("ServiceType")
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@JsonProperty("hasPets")
	public boolean getHasPets() {
		return hasPets;
	}

	@JsonProperty("hasPets")
	public void setHasPets(boolean hasPets) {
		this.hasPets = hasPets;
	}

	@JsonProperty("timeZone")
	public String getTimeZone() {
		return timeZone;
	}

	@JsonProperty("timeZone")
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	@JsonProperty("enableActiveService")
	public boolean getEnableActiveService() {
		return this.enableActiveService;
	}

	@JsonProperty("enableActiveService")
	public void setEnableActiveService(boolean enableActiveService) {
		this.enableActiveService = enableActiveService;
	}

	public boolean isSupportActiveService() {
		return supportActiveService;
	}

	@JsonProperty("supportActiveService")
	public void setSupportActiveService(boolean supportActiveService) {
		this.supportActiveService = supportActiveService;
	}

	public Date getFirstStepCountTime() {
		return firstStepCountTime;
	}

	@JsonProperty("firstStepCountTime")
	public void setFirstStepCountTime(Date firstStepCountTime) {
		this.firstStepCountTime = firstStepCountTime;
	}

	public boolean isAllowEmergencyCallModifications() {
		return allowEmergencyCallModifications;
	}

	@JsonProperty("allowEmergencyCallModifications")
	public void setAllowEmergencyCallModifications(boolean allowEmergencyCallModifications) {
		this.allowEmergencyCallModifications = allowEmergencyCallModifications;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public String getServicePackageCode() {
		return servicePackageCode;
	}

	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
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
}
