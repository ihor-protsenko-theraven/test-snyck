package com.essence.hc.eil.parsers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.util.Util;
import com.essence.hc.model.ExternalAPIResidentMonitoring;
import com.essence.hc.model.ExternalAPISystemStatus;
import com.essence.hc.model.ExternalAPISystemStatus.AlarmStatusType;
import com.essence.hc.model.ExternalAPISystemStatus.AlertStateType;
import com.essence.hc.model.User.Gender;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPIResidentMonitoringPrsr implements IParser<ExternalAPIResidentMonitoring> {

	private String account;
	private boolean hasStepCountData;
	private int panelId;
	private int userId;
	private int vendorId;
	private String firstName;
	private String lastName;
	private String identificationNumber;
	private String address;
	private String phone;
	private String mobile;
	private String gender;
	private String serviceProviderAccount;
	private String servicePackageCode;
	private String panelServicePackage;
	private boolean enableActiveService;
	private boolean isAdl;
	private String deviceType;
	private boolean isAtHome;
	private double movementLevel;
	private String lastLocation;
	private String alertStateType;
	private int alertSessionId;
	private String alertSeverity;
	private String alarmStatusType;
	private int alertNumber;
	private String latestLocationTime;
	
	public ExternalAPIResidentMonitoringPrsr() {
	}

	@Override
	public ExternalAPIResidentMonitoring parse() {

		ExternalAPIResidentMonitoring externalAPIResidentMonitoring = new ExternalAPIResidentMonitoring();

		try {
			externalAPIResidentMonitoring.setAccount(account);
			externalAPIResidentMonitoring.setStepCountData(hasStepCountData);
			externalAPIResidentMonitoring.setPanelId(panelId);
			externalAPIResidentMonitoring.setUserId(userId);
			externalAPIResidentMonitoring.setVendorId(vendorId);
			externalAPIResidentMonitoring.setFirstName(firstName);
			externalAPIResidentMonitoring.setLastName(lastName);
			externalAPIResidentMonitoring.setIdentificationNumber(identificationNumber);
			externalAPIResidentMonitoring.setAddress(address);
			externalAPIResidentMonitoring.setPhone(phone);
			externalAPIResidentMonitoring.setMobile(mobile);
			externalAPIResidentMonitoring.setGender(Gender.getGender(gender));
			externalAPIResidentMonitoring.setServiceProviderAccount(serviceProviderAccount);
			externalAPIResidentMonitoring.setServicePackageCode(servicePackageCode);
			externalAPIResidentMonitoring.setPanelServicePackage(panelServicePackage);
			externalAPIResidentMonitoring.setEnableActiveService(enableActiveService);
			externalAPIResidentMonitoring.setAdl(isAdl);
			externalAPIResidentMonitoring.setDeviceType(deviceType);
			externalAPIResidentMonitoring.setAtHome(isAtHome);
			externalAPIResidentMonitoring.setMovementLevel(movementLevel);
			externalAPIResidentMonitoring.setLastLocation(lastLocation);
			
			if ((latestLocationTime != null) && (!latestLocationTime.isEmpty())) {
				//Format Date String to Date
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = formatter.parse(latestLocationTime.substring(0,10) + " " + latestLocationTime.substring(11,16));
				externalAPIResidentMonitoring.setLatestLocationTime(date);
			}
			
			ExternalAPISystemStatus systemStatus = new ExternalAPISystemStatus();
			systemStatus.setAlarmStatusType(AlarmStatusType.parse(alarmStatusType));
			systemStatus.setAlertSessionId(alertSessionId);
			systemStatus.setAlertStateType(AlertStateType.parse(alertStateType));
			systemStatus.setAlertSeverity(alertSeverity);
			systemStatus.setAlertNumber(alertNumber);
			externalAPIResidentMonitoring.setSystemStatus(systemStatus);
			
			if ((lastLocation != null) && (lastLocation.contains(" - "))) {
				String[] lastLocationParts = lastLocation.split(" - ", 2);
				externalAPIResidentMonitoring.setLastLocationType(lastLocationParts[0]);
				externalAPIResidentMonitoring.setLastLocationDescription(lastLocationParts[1]);
			}
			
			externalAPIResidentMonitoring.setServiceType(ServiceType.fromString(
					Util.mapFromNewToOldServicePackageNaming(externalAPIResidentMonitoring.getServicePackageCode())));
			

		} catch (Exception ex) {
			throw new ParseException(ex, "Unexpected parse error");
		}
		return externalAPIResidentMonitoring;
	}

	@JsonProperty("account")
	public String getAccount() {
		return account;
	}

	@JsonProperty("account")
	public void setAccount(String account) {
		this.account = account;
	}

	@JsonProperty("hasStepCountData")
	public boolean hasStepCountData() {
		return hasStepCountData;
	}

	@JsonProperty("hasStepCountData")
	public void setStepCountData(boolean hasStepCountData) {
		this.hasStepCountData = hasStepCountData;
	}

	@JsonProperty("panelId")
	public int getPanelId() {
		return panelId;
	}

	@JsonProperty("panelId")
	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

	@JsonProperty("userId")
	public int getUserId() {
		return userId;
	}

	@JsonProperty("userId")
	public void setUserId(int userId) {
		this.userId = userId;
	}

	@JsonProperty("vendorId")
	public int getVendorId() {
		return vendorId;
	}

	@JsonProperty("vendorId")
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("identificationNumber")
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	@JsonProperty("identificationNumber")
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	@JsonProperty("address")
	public String getAddress() {
		return address;
	}

	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}

	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}

	@JsonProperty("phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonProperty("mobile")
	public String getMobile() {
		return mobile;
	}

	@JsonProperty("mobile")
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JsonProperty("gender")
	public String getGender() {
		return gender;
	}

	@JsonProperty("gender")
	public void setGender(String gender) {
		this.gender = gender;
	}

	@JsonProperty("serviceProviderAccount")
	public String getServiceProviderAccount() {
		return serviceProviderAccount;
	}

	@JsonProperty("serviceProviderAccount")
	public void setServiceProviderAccount(String serviceProviderAccount) {
		this.serviceProviderAccount = serviceProviderAccount;
	}

	@JsonProperty("servicePackageCode")
	public String getServicePackageCode() {
		return servicePackageCode;
	}

	@JsonProperty("servicePackageCode")
	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}

	@JsonProperty("panelServicePackage")
	public String getPanelServicePackage() {
		return panelServicePackage;
	}

	@JsonProperty("panelServicePackage")
	public void setPanelServicePackage(String panelServicePackage) {
		this.panelServicePackage = panelServicePackage;
	}

	@JsonProperty("enableActiveService")
	public boolean isEnableActiveService() {
		return enableActiveService;
	}

	@JsonProperty("enableActiveService")
	public void setEnableActiveService(boolean enableActiveService) {
		this.enableActiveService = enableActiveService;
	}

	@JsonProperty("isAdl")
	public boolean isAdl() {
		return isAdl;
	}

	@JsonProperty("isAdl")
	public void setAdl(boolean isAdl) {
		this.isAdl = isAdl;
	}

	@JsonProperty("deviceType")
	public String getDeviceType() {
		return deviceType;
	}

	@JsonProperty("deviceType")
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	@JsonProperty("isAtHome")
	public boolean isAtHome() {
		return isAtHome;
	}

	@JsonProperty("isAtHome")
	public void setAtHome(boolean isAtHome) {
		this.isAtHome = isAtHome;
	}

	@JsonProperty("movementLevel")
	public double getMovementLevel() {
		return movementLevel;
	}

	@JsonProperty("movementLevel")
	public void setMovementLevel(double movementLevel) {
		this.movementLevel = movementLevel;
	}

	@JsonProperty("lastLocation")
	public String getLastLocation() {
		return lastLocation;
	}

	@JsonProperty("lastLocation")
	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	@JsonProperty("alertStateType")
	public String getAlertStateType() {
		return alertStateType;
	}

	@JsonProperty("alertStateType")
	public void setAlertStateType(String alertStateType) {
		this.alertStateType = alertStateType;
	}

	@JsonProperty("alertSessionId")
	public int getAlertSessionId() {
		return alertSessionId;
	}

	@JsonProperty("alertSessionId")
	public void setAlertSessionId(int alertSessionId) {
		this.alertSessionId = alertSessionId;
	}

	@JsonProperty("alertSeverity")
	public String getAlertSeverity() {
		return alertSeverity;
	}

	@JsonProperty("alertSeverity")
	public void setAlertSeverity(String alertSeverity) {
		this.alertSeverity = alertSeverity;
	}

	@JsonProperty("alarmStatusType")
	public String getAlarmStatusType() {
		return alarmStatusType;
	}

	@JsonProperty("alarmStatusType")
	public void setAlarmStatusType(String alarmStatusType) {
		this.alarmStatusType = alarmStatusType;
	}

	@JsonProperty("alertNumber")
	public int getAlertNumber() {
		return alertNumber;
	}

	@JsonProperty("alertNumber")
	public void setAlertNumber(int alertNumber) {
		this.alertNumber = alertNumber;
	}

	@JsonProperty("latestLocationTime")
	public String getLatestLocationTime() {
		return latestLocationTime;
	}

	@JsonProperty("latestLocationTime")
	public void setLatestLocationTime(String latestLocationTime) {
		this.latestLocationTime = latestLocationTime;
	}
	
}
