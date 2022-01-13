package com.essence.hc.eil.parsers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.AlertPreferences;
//import com.essence.hc.model.AlertPreferences.AlertPrefsType;

/**
 * @author oscar.canalejo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertPreferencesPrsr implements IParser<AlertPreferences> {

	private static final String SMS_NOTIFICATION = "alertNotificationSMS";
	private static final String EMAIL_NOTIFICATION = "alertNotificationEmail";
	private static final String SMS_REPORT = "dailyReportSMS";
	private static final String EMAIL_REPORT = "dailyReportEmail";

	
	private List<BasicAlertPreferencesPrsr> basicAlertPreferences;
	private boolean dailyReportSMS;
	private boolean dailyReportEmail;
    private boolean alertNotificationSMS;
    private boolean alertNotificationEmail;
    
	
	@Override
	public AlertPreferences parse() {

		AlertPreferences preferences = null;
		Map<String, Boolean> basicAlertPrefs = null;
		
		if(basicAlertPreferences != null)
		{
			AlertConfigPrsr alrtConfigPrsr = new AlertConfigPrsr();
			alrtConfigPrsr.setBasicAlertPreferences(basicAlertPreferences);
			if ((basicAlertPrefs = alrtConfigPrsr.parse()) != null) {
				preferences = getBasicPrefs(basicAlertPrefs);
			}
		}
		
		Map<String, Boolean> alertReportNotifications = new LinkedHashMap<String, Boolean>();			
			alertReportNotifications.put(SMS_NOTIFICATION, alertNotificationSMS);
			alertReportNotifications.put(EMAIL_NOTIFICATION, alertNotificationEmail);
			
		Map<String, Boolean> dailyReportNotifications = new LinkedHashMap<String, Boolean>();						
			dailyReportNotifications.put(SMS_REPORT, dailyReportSMS);	
			dailyReportNotifications.put(EMAIL_REPORT, dailyReportEmail);

			preferences.setAlertReportNotifications(alertReportNotifications);
			preferences.setDailyReportNotifications(dailyReportNotifications);
	
		return preferences;
	}

	
	public static AlertPreferences getBasicPrefs(Map<String, Boolean> basicAlertPrefs) {
		
		Map<String, Boolean> activityAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> emergencyAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> safetyAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> securityAlerts = new LinkedHashMap<>();
		Map<String, Boolean> technicalAlerts = new LinkedHashMap<String, Boolean>();
		Map<String, Boolean> advisoryAlerts = new LinkedHashMap<String, Boolean>();
		
		if (basicAlertPrefs != null) {
			/*
			 * Activity ----
			 */
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("PossibleFall"),basicAlertPrefs, activityAlerts);
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("LowActivity"),basicAlertPrefs, activityAlerts);			
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("DoorOpen"),basicAlertPrefs, activityAlerts);
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("NotAtHome"),basicAlertPrefs, activityAlerts);			
//			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("Activity"),basicAlertPrefs, activityAlerts);
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("AbnormalActivity"),basicAlertPrefs, activityAlerts);
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("ExcessiveActivity"),basicAlertPrefs, activityAlerts);
			/*
			 * Emergency ---
			 */
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("Panic"),basicAlertPrefs, emergencyAlerts);
			/*
			 * Safety ----
			 */
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("SmokeAlarm"),basicAlertPrefs, safetyAlerts);
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("WaterLeakageAlarm"),basicAlertPrefs, safetyAlerts);
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("ExtremeTemperature"),basicAlertPrefs, safetyAlerts);
			/*
			 * Technical ----
			 */
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("Technical"),basicAlertPrefs, technicalAlerts);
			/*
			 * Security ----
			 */
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("UnexpectedEntryExit"),basicAlertPrefs, securityAlerts);
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("AtHomeForTooLong"), basicAlertPrefs, securityAlerts);
			
			/*
			 * Advisory Alerts (the old "Presence" preference in Security has now moved to "Wake and Well" in Safety Alerts
			 * but in the backend API is still using the same field
			 */
			copyElement(AlertConfigPrsr.ALERT_PREFS_NAMES.get("Presence"),basicAlertPrefs, advisoryAlerts);
			
		}
		
		AlertPreferences preferences = new AlertPreferences();
			preferences.setActivityAlerts(activityAlerts);
			preferences.setEmergencyAlerts(emergencyAlerts);
			preferences.setSecurityAlerts(securityAlerts);
			preferences.setSafetyAlerts(safetyAlerts);
			preferences.setTechnicalAlerts(technicalAlerts);
			preferences.setAdvisoryAlerts(advisoryAlerts);
	
		return preferences;		
	}
	
	/**
	 * Copy a key/value element from one Map to another. 
	 * The copy will only take effect If the specified key exists in the source Map 
	 * @param key Map identifier for the element to be copied
	 * @param fromMap Source Map
	 * @param toMap Target Map
	 */
	private static void copyElement(String key, Map<String, Boolean> fromMap, Map<String, Boolean> toMap) {
		if (fromMap.containsKey(key))
			toMap.put(key,fromMap.get(key));
	}
	
	
	public boolean isDailyReportSMS() {
		return dailyReportSMS;
	}

	public void setDailyReportSMS(boolean dailyReportSMS) {
		this.dailyReportSMS = dailyReportSMS;
	}

	public boolean isDailyReportEmail() {
		return dailyReportEmail;
	}

	public void setDailyReportEmail(boolean dailyReportEmail) {
		this.dailyReportEmail = dailyReportEmail;
	}

	public boolean isAlertNotificationSMS() {
		return alertNotificationSMS;
	}

	public void setAlertNotificationSMS(boolean alertNotificationSMS) {
		this.alertNotificationSMS = alertNotificationSMS;
	}

	public boolean isAlertNotificationEmail() {
		return alertNotificationEmail;
	}

	public void setAlertNotificationEmail(boolean alertNotificationEmail) {
		this.alertNotificationEmail = alertNotificationEmail;
	}

	@JsonProperty("AlertPreferences")
	public List<BasicAlertPreferencesPrsr> getBasicAlertPreferences() {
		return basicAlertPreferences;
	}

	@JsonProperty("AlertPreferences")
	public void setBasicAlertPreferences(List<BasicAlertPreferencesPrsr> alertPreferences) {
		basicAlertPreferences = alertPreferences;
	}

}
