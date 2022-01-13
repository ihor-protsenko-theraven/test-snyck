package com.essence.hc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.model.Alert.AlertState;
import com.essence.hc.model.Report.ReportType;
import com.essence.hc.model.Vendor.ServiceType;

/**
 * Person who is being monitored.
 * His/Her diary habits an home activity are being registered and managed.
 * 
 * @author oscar.canalejo
 */
public class Patient extends SystemUser implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private String cpId;
	private String City;
	private String State;
	
	private Installation installation;
	private List<Event> events;
	private SystemStatus systemStatus;
	private ActivityLevel activityLevel;
	private int numAlerts;
	private int iLastAlrtId;
	private AlertState minAlertState;
	private String panelTime;
	private PatientSettings patientSettings;
	private String residentCode;
	private boolean isActiveService;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// Carrier for the service type info. 
	private String servicePackageCode;
	
	// Java app should be panelServicePackages agnostic. It is not going to be processed so far
	private String panelServicePackages;
	
	private List<ReportType> availableReports;
	
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public Installation getInstallation() {
		return installation;
	}
	public void setInstallation(Installation installation) {
		this.installation = installation;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public SystemStatus getSystemStatus() {
		return systemStatus;
	}
	public void setSystemStatus(SystemStatus systemStatus) {
		this.systemStatus = systemStatus;
	}
	public ActivityLevel getActivityLevel() {
		return activityLevel;
	}
	public void setActivityLevel(ActivityLevel activityLevel) {
		this.activityLevel = activityLevel;
	}
	public int getNumAlerts() {
		return numAlerts;
	}
	public void setNumAlerts(int numAlerts) {
		this.numAlerts = numAlerts;
	}
	public int getiLastAlrtId() {
		return iLastAlrtId;
	}
	public void setiLastAlrtId(int iLastAlrtId) {
		this.iLastAlrtId = iLastAlrtId;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public AlertState getMinAlertState() {
		return minAlertState;
	}
	public void setMinAlertState(AlertState minAlertState) {
		this.minAlertState = minAlertState;
	}
	public String getPanelTime() {
		return panelTime;
	}
	
	public Date currentPanelDay() {
		String delims = "[-T:+-]";
		String dayFormatted = "";
		String[] dateComponents = this.panelTime.split(delims);
		
		Date panelDate = new Date();
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(dateComponents[0]), Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]), 0, 0); 
		panelDate = c.getTime();
		
		return panelDate;
	}
	
	public void setPanelTime(String panelTime) {
		this.panelTime = panelTime;
	}
	public PatientSettings getPatientSettings() {
		return patientSettings;
	}
	public void setPatientSettings(PatientSettings patitentSettings) {
		this.patientSettings = patitentSettings;
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
	
	public void setIsActiveService(boolean isActiveService) {
		this.isActiveService = isActiveService;
	}
	public boolean getIsActiveService() {
		return isActiveService;
	}
	
	
	// TODO this should be done with a factory pattern, in order to create ProPatient and FamilyPatient
	// with the default available reports for each kind of patient.
	// Decided to implement this method because is risky and not time to adapt the invokation of Patient class
	// on APIparsers.
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
	public String getResidentCode() {
		return residentCode;
	}
	public void setResidentCode(String residentCode) {
		this.residentCode = residentCode;
	}
	public String getServicePackageCode() {
		return servicePackageCode;
	}
	public void setServicePackageCode(String servicePackageCode) {
		this.servicePackageCode = servicePackageCode;
	}
	public String getPanelServicePackages() {
		return panelServicePackages;
	}
	public void setPanelServicePackages(String panelServicePackages) {
		this.panelServicePackages = panelServicePackages;
	}
	
}
