/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;

/**
 * ADL External API System Status
 *
 */
public class ExternalAPISystemStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static enum AlarmStatusType { 
		E_NO_ALERTS(0), E_ALARM(1), E_ALARM_IN_PROGRESS(2), E_TECH_ALARM(3), E_USER_PHOTO_REQUEST(4); 
		private final int id;
		AlarmStatusType(int id) { this.id = id;}
		public int getId() {return id;}
		public static AlarmStatusType parse(String type) {
			switch(type) {
		        case "E_NO_ALERTS": return E_NO_ALERTS;
		        case "E_ALARM": return E_ALARM;
		        case "E_ALARM_IN_PROGRESS": return E_ALARM_IN_PROGRESS;
		        case "E_TECH_ALARM": return E_TECH_ALARM;
		        case "E_USER_PHOTO_REQUEST": return E_USER_PHOTO_REQUEST;
		    }
		    return null;
		} 
	}
	
	public static enum AlertStateType { 
		ST_NEW(0), ST_VIEWED(1), ST_IN_PROGRESS(2), ST_CLOSED(3);
		private final int id;
		AlertStateType(int id) { this.id = id;}
		public int getId() {return id;}
		public static AlertStateType parse(String type) {
			switch(type) {
		        case "ST_NEW": return ST_NEW;
		        case "ST_VIEWED": return ST_VIEWED;
		        case "ST_IN_PROGRESS": return ST_IN_PROGRESS;
		        case "ST_CLOSED": return ST_CLOSED;
		    }
		    return null;
		} 
	}
		
	private AlarmStatusType alarmStatusType;
	private int alertSessionId;
	private AlertStateType alertStateType;
	private String alertSeverity;
	private int alertNumber;
	
	public AlarmStatusType getAlarmStatusType() {
		return alarmStatusType;
	}
	public void setAlarmStatusType(AlarmStatusType alarmStatusType) {
		this.alarmStatusType = alarmStatusType;
	}
	public int getAlertSessionId() {
		return alertSessionId;
	}
	public void setAlertSessionId(int alertSessionId) {
		this.alertSessionId = alertSessionId;
	}
	public AlertStateType getAlertStateType() {
		return alertStateType;
	}
	public void setAlertStateType(AlertStateType alertStateType) {
		this.alertStateType = alertStateType;
	}
	public String getAlertSeverity() {
		return alertSeverity;
	}
	public void setAlertSeverity(String alertSeverity) {
		this.alertSeverity = alertSeverity;
	}
	public int getAlertNumber() {
		return alertNumber;
	}
	public void setAlertNumber(int alertNumber) {
		this.alertNumber = alertNumber;
	}	

}
