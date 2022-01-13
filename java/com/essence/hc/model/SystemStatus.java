/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;
import java.util.List;

import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Alert.AlertState;

/**
 * ADL System Status
 * State of whole set of components located at the patient's home, 
 * 
 * @author daniel.alcantarilla
 *
 */
public class SystemStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static enum StatusTypes{ NO_ALERTS(0),ALARM(1),ALARM_IN_PROGRESS(2),TECH_ALARM(3),USER_PHOTO_REQUEST(4),ADVISORY(5); 
		private final int status;
		StatusTypes(int status) { this.status = status;}
		public int getStatus() {return status;}
	}
	
	private int alertSessionId;
	private int numAlert;
	private StatusTypes statusType;
	private Alert alert;
	private AlertState sysState; 
	private String severity;

	public int getNumAlert() {
		return numAlert;
	}

	public void setNumAlert(int numAlert) {
		this.numAlert = numAlert;
	}

	public AlertState getSysState() {
		return sysState;
	}

	public void setSysState(AlertState sysState) {
		this.sysState = sysState;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public StatusTypes getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusTypes statusType) {
		this.statusType = statusType;
	}

	public int getAlertSessionId() {
		return alertSessionId;
	}

	public void setAlertSessionId(int alertSessionId) {
		this.alertSessionId = alertSessionId;
	}

	
}
