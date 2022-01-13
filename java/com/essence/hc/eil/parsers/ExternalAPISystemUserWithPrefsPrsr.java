package com.essence.hc.eil.parsers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.model.AlertPreferences;
import com.essence.hc.model.SystemUser;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPISystemUserWithPrefsPrsr implements IParser<SystemUser> {
	
	private static final String SMS_NOTIFICATION = "alertNotificationSMS";
	private static final String EMAIL_NOTIFICATION = "alertNotificationEmail";
	private static final String SMS_REPORT = "dailyReportSMS";
	private static final String EMAIL_REPORT = "dailyReportEmail";
	
	private ExternalAPISystemUserPrsr userDetails;
	private Map<String, Boolean> alertPreferences;
	private Map<String, Boolean> communicationMethods;

	@Override
	public SystemUser parse() {
		SystemUser user = userDetails.parse();
		// TODO: set alert prefs
		Map<String, Boolean> activityAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> emergencyAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> safetyAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> securityAlerts = new LinkedHashMap<>();
		Map<String, Boolean> technicalAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> advisoryAlerts = new LinkedHashMap<String, Boolean>();
		AlertPreferences preferences = new AlertPreferences();
			preferences.setActivityAlerts(activityAlerts);
			preferences.setEmergencyAlerts(emergencyAlerts);
			preferences.setSecurityAlerts(securityAlerts);
			preferences.setSafetyAlerts(safetyAlerts);
			preferences.setTechnicalAlerts(technicalAlerts);
			preferences.setAdvisoryAlerts(advisoryAlerts);
			
		if (alertPreferences != null) {
			/*
			 * Activity ----
			 */
			copyElement("possibleFall", AlertConfigPrsr.ALERT_PREFS_NAMES.get("PossibleFall"),alertPreferences, activityAlerts);
			copyElement("lowActivity", AlertConfigPrsr.ALERT_PREFS_NAMES.get("LowActivity"),alertPreferences, activityAlerts);			
			copyElement("doorOpen", AlertConfigPrsr.ALERT_PREFS_NAMES.get("DoorOpen"),alertPreferences, activityAlerts);
			copyElement("notAtHome", AlertConfigPrsr.ALERT_PREFS_NAMES.get("NotAtHome"),alertPreferences, activityAlerts);			
			copyElement("abnormalActivity", AlertConfigPrsr.ALERT_PREFS_NAMES.get("AbnormalActivity"),alertPreferences, activityAlerts);
			copyElement("excessiveActivity", AlertConfigPrsr.ALERT_PREFS_NAMES.get("ExcessiveActivity"),alertPreferences, activityAlerts);
			/*
			 * Emergency 
			 */
			copyElement("sos", AlertConfigPrsr.ALERT_PREFS_NAMES.get("Panic"), alertPreferences, emergencyAlerts);
			/*
			 * Safety ----
			 */
			copyElement("smoke", AlertConfigPrsr.ALERT_PREFS_NAMES.get("SmokeAlarm"),alertPreferences, safetyAlerts);
			copyElement("waterLeakage", AlertConfigPrsr.ALERT_PREFS_NAMES.get("WaterLeakageAlarm"),alertPreferences, safetyAlerts);
			copyElement("extremeTemperature", AlertConfigPrsr.ALERT_PREFS_NAMES.get("ExtremeTemperature"),alertPreferences, safetyAlerts);
			/*
			 * Technical ----
			 */
			copyElement("technical", AlertConfigPrsr.ALERT_PREFS_NAMES.get("Technical"),alertPreferences, technicalAlerts);
			/*
			 * Security ----
			 */
			copyElement("unexpectedEntryExit", AlertConfigPrsr.ALERT_PREFS_NAMES.get("UnexpectedEntryExit"),alertPreferences, securityAlerts);
			copyElement("atHomeForTooLong", AlertConfigPrsr.ALERT_PREFS_NAMES.get("AtHomeForTooLong"),alertPreferences, securityAlerts);
			
			/*
			 * Advisory Alerts (the old "Presence" preference in Security has now moved to "Wake and Well" in Safety Alerts
			 * but in the backend API is still using the same field
			 */
			copyElement("presence",AlertConfigPrsr.ALERT_PREFS_NAMES.get("Presence"),alertPreferences, advisoryAlerts);
		}
		
		if (communicationMethods != null) {
			Map<String, Boolean> alertReportNotifications = new LinkedHashMap<String, Boolean>();
				copyElement("sendAlertsBySMS", SMS_NOTIFICATION, communicationMethods, alertReportNotifications);
				copyElement("sendAlertsByEmail", EMAIL_NOTIFICATION, communicationMethods, alertReportNotifications);
				
			Map<String, Boolean> dailyReportNotifications = new LinkedHashMap<String, Boolean>();					
				copyElement("sendReportsBySMS", SMS_REPORT, communicationMethods, dailyReportNotifications);
				copyElement("sendReportsByEmail", EMAIL_REPORT, communicationMethods, dailyReportNotifications);
	
			preferences.setAlertReportNotifications(alertReportNotifications);
			preferences.setDailyReportNotifications(dailyReportNotifications);

		}
		
		user.setAlertPrefs(preferences);
		return user;
	}
	
	/**
	 * Copy a key/value element from one Map to another. 
	 * The copy will only take effect If the specified key exists in the source Map 
	 * @param fromKey Map identifier for the element to be copied
	 * @param fromMap Source Map
	 * @param toMap Target Map
	 */
	private static void copyElement(String fromKey, String toKey, Map<String, Boolean> fromMap, Map<String, Boolean> toMap) {
		if (fromMap.containsKey(fromKey))
			toMap.put(toKey,fromMap.get(fromKey));
	}

	public ExternalAPISystemUserPrsr getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(ExternalAPISystemUserPrsr userDetails) {
		this.userDetails = userDetails;
	}

	public Map<String, Boolean> getAlertPreferences() {
		return alertPreferences;
	}

	public void setAlertPreferences(Map<String, Boolean> alertPreferences) {
		this.alertPreferences = alertPreferences;
	}

	public Map<String, Boolean> getCommunicationMethods() {
		return communicationMethods;
	}

	public void setCommunicationMethods(Map<String, Boolean> communicationMethods) {
		this.communicationMethods = communicationMethods;
	}

}
