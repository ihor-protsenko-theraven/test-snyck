/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


/**
 * 
 * @author daniel.alcantarilla
 * @author oscar.canalejo
 *
 */
public class AlertPreferences extends Event implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Alert Preferences Types Enumeration
	 */
	/*
	public static enum AlertPrefsType { SmokeAlarm, Panic, PossibleFall, LowActivity, WaterLeakageAlarm, 
		DoorOpen, NotAtHome, Technical, ActivityAlert, UnexpectedEntryExit, ExtremeTemperature,
		Presence, ExcessiveActivity, AbnormalActivity }
	*/
	
	Map<String, Boolean> activityAlerts = new HashMap<String, Boolean>();
	Map<String, Boolean> emergencyAlerts = new HashMap<String, Boolean>();
	Map<String, Boolean> securityAlerts = new HashMap<String, Boolean>();
	Map<String, Boolean> safetyAlerts = new HashMap<String, Boolean>();
	Map<String, Boolean> technicalAlerts = new HashMap<String, Boolean>();
	Map<String, Boolean> advisoryAlerts = new HashMap<String, Boolean>();
	
	Map<String, Boolean> dailyReportNotifications = new LinkedHashMap<String, Boolean>();
	Map<String, Boolean> alertReportNotifications = new LinkedHashMap<String, Boolean>();
	
	
	
	public Map<String, Boolean> getSecurityAlerts() {
		return securityAlerts;
	}
	public void setSecurityAlerts(Map<String, Boolean> securityAlerts) {
		this.securityAlerts = securityAlerts;
	}
	public Map<String, Boolean> getSafetyAlerts() {
		return safetyAlerts;
	}
	public void setSafetyAlerts(Map<String, Boolean> safetyAlerts) {
		this.safetyAlerts = safetyAlerts;
	}
	public Map<String, Boolean> getTechnicalAlerts() {
		return technicalAlerts;
	}
	public void setTechnicalAlerts(Map<String, Boolean> technicalAlerts) {
		this.technicalAlerts = technicalAlerts;
	}
	
	public Map<String, Boolean> getAdvisoryAlerts() {
		return advisoryAlerts;
	}
	public void setAdvisoryAlerts(Map<String, Boolean> advisoryAlerts) {
		this.advisoryAlerts = advisoryAlerts;
	}
	
	public Map<String, Boolean> getDailyReportNotifications() {
		return dailyReportNotifications;
	}
	public void setDailyReportNotifications(
			Map<String, Boolean> dailyReportNotifications) {
		this.dailyReportNotifications = dailyReportNotifications;
	}
	public Map<String, Boolean> getAlertReportNotifications() {
		return alertReportNotifications;
	}
	public void setAlertReportNotifications(
			Map<String, Boolean> alertReportNotifications) {
		this.alertReportNotifications = alertReportNotifications;
	}
	public Map<String, Boolean> getActivityAlerts() {
		return activityAlerts;
	}
	public void setActivityAlerts(Map<String, Boolean> activityAlerts) {
		this.activityAlerts = activityAlerts;
	}
	public Map<String, Boolean> getEmergencyAlerts() {
		return emergencyAlerts;
	}
	public void setEmergencyAlerts(Map<String, Boolean> emergencyAlerts) {
		this.emergencyAlerts = emergencyAlerts;
	}
	
	
	
	
}
