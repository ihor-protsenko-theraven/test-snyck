package com.essence.hc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.essence.hc.model.Report.ReportType;
import com.essence.hc.model.User.Gender;
import com.essence.hc.model.Vendor.ServiceType;

public class ExternalAPIResidentMonitoring {
	
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
	private Gender gender;
	private String serviceProviderAccount;
	private String servicePackageCode;
	private String panelServicePackage;
	private boolean enableActiveService;
	private boolean isAdl;
	private String deviceType;
	private boolean isAtHome;
	private double movementLevel;
	private String lastLocation;
	private Date latestLocationTime;
	private ExternalAPISystemStatus systemStatus;
	
	private String lastLocationType;
	private String lastLocationDescription;
	private ServiceType serviceType;
	private List<ReportType> availableReports;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean hasStepCountData() {
		return hasStepCountData;
	}

	public void setStepCountData(boolean hasStepCountData) {
		this.hasStepCountData = hasStepCountData;
	}

	public int getPanelId() {
		return panelId;
	}

	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getServiceProviderAccount() {
		return serviceProviderAccount;
	}

	public void setServiceProviderAccount(String serviceProviderAccount) {
		this.serviceProviderAccount = serviceProviderAccount;
	}

	public String getServicePackageCode() {
		return servicePackageCode;
	}

	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}

	public String getPanelServicePackage() {
		return panelServicePackage;
	}

	public void setPanelServicePackage(String panelServicePackage) {
		this.panelServicePackage = panelServicePackage;
	}

	public boolean isEnableActiveService() {
		return enableActiveService;
	}

	public void setEnableActiveService(boolean enableActiveService) {
		this.enableActiveService = enableActiveService;
	}

	public boolean isAdl() {
		return isAdl;
	}

	public void setAdl(boolean isAdl) {
		this.isAdl = isAdl;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isAtHome() {
		return isAtHome;
	}

	public void setAtHome(boolean isAtHome) {
		this.isAtHome = isAtHome;
	}

	public double getMovementLevel() {
		return movementLevel;
	}

	public void setMovementLevel(double movementLevel) {
		this.movementLevel = movementLevel;
	}

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	public Date getLatestLocationTime() {
		return latestLocationTime;
	}

	public void setLatestLocationTime(Date latestLocationTime) {
		this.latestLocationTime = latestLocationTime;
	}

	public ExternalAPISystemStatus getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(ExternalAPISystemStatus systemStatus) {
		this.systemStatus = systemStatus;
	}

	public String getLastLocationType() {
		return lastLocationType;
	}

	public void setLastLocationType(String lastLocationType) {
		// Transformation to adapt express names to their correspondence in analytics, since the returned values are different, but we want the same messages and icons
		// FrontDoor is a special case, because when last location is FrontDoor for express we show the door icon, while FRONT_DOOR is considered an indication of out of home in analytics
		switch (lastLocationType) {
			case "Unknown": this.lastLocationType = "OTHER_ROOM"; break;
			case "FridgeDoor": this.lastLocationType = "FRIDGE_DOOR"; break;
			case "Bedroom": this.lastLocationType = "BEDROOM_SENSOR"; break;
			case "ToiletRoom": 
			case "Restroom": this.lastLocationType = "TOILET_ROOM_SENSOR"; break;
			case "Bathroom": this.lastLocationType = "BATHROOM"; break;
			case "LivingRoom": this.lastLocationType = "LIVING_ROOM"; break;
			case "DiningRoom": this.lastLocationType = "DINING_ROOM"; break;
			default: this.lastLocationType = lastLocationType;
		}
	}

	public String getLastLocationDescription() {
		return lastLocationDescription;
	}

	public void setLastLocationDescription(String lastLocationDescription) {
		this.lastLocationDescription = lastLocationDescription;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public List<ReportType> getAvailableReports() {
		return availableReports;
	}
	public void setAvailableReports(List<ReportType> availableReports) {
		this.availableReports = availableReports;
	}
	
	public void addAvailableReport(ReportType reportType) {
		
		if(this.availableReports == null) {
			this.availableReports = new ArrayList<ReportType>();
		}
		
		if(this.availableReports.indexOf(reportType) == -1) {
			this.availableReports.add(reportType);
		}
		
	}
	
	public void removeAvailableReport(ReportType reportType) {
		if(this.availableReports != null) {
			if(this.availableReports.indexOf(reportType) != -1) {
				this.availableReports.remove(this.availableReports.indexOf(reportType));
			}
		}
	}
	
	//TODO hotfix (2.5.10)
	public List<ReportType> obtainDefaultAvailableReports(){
		
		List<ReportType> availableReportTypes = new ArrayList<ReportType>();
			
			if(serviceType != null) {
			
				if(serviceType == ServiceType.EXPRESS) {
					availableReportTypes.add(ReportType.DAY_STORY_EXPRESS);
					availableReportTypes.add(ReportType.WEEKLY_ACTIVITY);
					availableReportTypes.add(ReportType.MONTHLY_REPORT);
				}
				
				if(serviceType == ServiceType.ANALYTICS) {
					availableReportTypes.add(ReportType.DAY_STORY_ANALYTICS);
					availableReportTypes.add(ReportType.MOVEMENT_LEVEL);
					availableReportTypes.add(ReportType.WEEKLY_REPORT);
				}
			}
		
		return availableReportTypes;
	}
}
