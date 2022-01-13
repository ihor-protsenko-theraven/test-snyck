package com.essence.hc.eil.parsers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

//import com.essence.hc.model.AlertPreferences.AlertPrefsType;

/**
 * @author oscar.canalejo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertConfigPrsr implements IParser<Map<String, Boolean>> {

	private List<BasicAlertPreferencesPrsr> basicAlertPreferences;

	/*
	 * Matching for legacy alert prefs names
	 */
	public static final Map<String, String> ALERT_PREFS_NAMES = new LinkedHashMap<>();

	static {
		ALERT_PREFS_NAMES.put("SmokeAlarm", "bGetSmoke");
		ALERT_PREFS_NAMES.put("Panic", "bGetPanic");
		ALERT_PREFS_NAMES.put("PossibleFall", "bGetFall");
		ALERT_PREFS_NAMES.put("LowActivity", "bGetActvt");
		ALERT_PREFS_NAMES.put("WaterLeakageAlarm", "bGetWtLkge");
		ALERT_PREFS_NAMES.put("DoorOpen", "bGetDoor");
		ALERT_PREFS_NAMES.put("NotAtHome", "bGetNtAtHm");
		ALERT_PREFS_NAMES.put("Technical", "bGetTech");

		// ALERT_PREFS_NAMES.put("Activity", "Activity");
		ALERT_PREFS_NAMES.put("ExtremeTemperature", "ExtremeTemperature");
		ALERT_PREFS_NAMES.put("UnexpectedEntryExit", "UnexpectedEntryExit");
		ALERT_PREFS_NAMES.put("AtHomeForTooLong", "AtHomeForTooLong");

		ALERT_PREFS_NAMES.put("AbnormalActivity", "AbnormalActivity");
//		ALERT_PREFS_NAMES.put("LowActivity", "LowActivity");
		ALERT_PREFS_NAMES.put("ExcessiveActivity", "ExcessiveActivity");
		ALERT_PREFS_NAMES.put("Presence", "Presence");
	}

	@Override
	public Map<String, Boolean> parse() {

		Map<String, Boolean> alertPrefs = new LinkedHashMap<String, Boolean>();
		for (BasicAlertPreferencesPrsr basicAlertPref : basicAlertPreferences) {
			alertPrefs.put(ALERT_PREFS_NAMES.get(basicAlertPref.getAlertType()), basicAlertPref.isHandled());
		}
		return alertPrefs;
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
