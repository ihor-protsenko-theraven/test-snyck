package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

//import com.essence.hc.model.AlertPreferences.AlertPrefsType;

/*
 * Nested class for basic alert preferences parsing
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicAlertPreferencesPrsr {
	
	private String alertType;
	private boolean handled;
	

	public BasicAlertPreferencesPrsr() {
		super();
	}

	@JsonProperty("AlertType")
	public String getAlertType() {
		return alertType;
	}

	@JsonProperty("AlertType")
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	@JsonProperty("Handled")
	public boolean isHandled() {
		return handled;
	}

	@JsonProperty("Handled")
	public void setHandled(boolean handled) {
		this.handled = handled;
	}
}
