/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;
import java.util.Calendar;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Any remarkable occurrence, directly fired by the panel 
 * or derived from an {@link Activity} (e.g: lack or abuse of meds dispenser). 
 * When an Alert is fired, the system provides a set of tools ({@link Action}) 
 * for the Care Givers to manage the alert.
 * 
 * This model entity is a superclass for all alert types.
 * 
 * @author oscar.canalejo
 *
 */
public class Alert extends Event implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Alert Types Enumeration
	 */
	public static enum AlertType { ACTIVITY(1),SOS(2),VIDEO(3),TECHNICAL(4),ADVISORY(5),USER(8);
		 private final int id;
		 AlertType(int id) { this.id = id;}
		 public int getId() {return id;}
		 
		 public static AlertType getById(int id) {
			    for(AlertType alertType : values()) {
			        if(alertType.getId() == id){
			        	return alertType;
			        }
			    }
			    return null;
			 } 
	}
	
	public static enum AlertState{ NEW(0),VIEWED(1),IN_PROGRESS(2),CLOSED(3);
		 private final int id;
		 AlertState(int id) { this.id = id;}
		 public int getId() {return id;}
		 public static AlertState parseInt(int intValue) {
		        switch(intValue) {
		        case 0:
		            return NEW;
		        case 1:
		            return VIEWED;
		        case 2:
		            return IN_PROGRESS;
		        case 3:
		            return CLOSED;
		        }
		        return null;
		    }		 
	}
	
	public static enum ManualAlertType 
	{
		MANUAL_ALERT_TYPE_MEDICAL ("Medical"), 
		MANUAL_ALERT_TYPE_TECHNICAL_GENERAL ("TechnicalGeneral"),
		MANUAL_ALERT_TYPE_TECHNICAL_DEVICE ("TechnicalDevice");
		
		private final String value;

	    ManualAlertType(String x)
	    {
	    	 value = x;
	    }
	    
	    public String getValue() 
	    {
	    	return value; 
	    }
	}
	
	public static enum IssueType{ RESIDENT_SICK(1), GENERIC_INSTALLATION_ISSUE(2), DEVICE_ISSUE(3);
	 private final int id;
	 IssueType(int id) { this.id = id;}
	 public int getId() {return id;}
	}
	
	public static enum AlertHandlingConclusion{HANDLED_BY_USER("HandledByUser", 1), HANDLED_BY_OPERATOR("HandledByOperator", 2), 
		FALSE_ALARM_BY_USER("FalseAlarmByUser", 3), FALSE_ALARM_BY_OPERATOR("FalseAlarmByOperator", 4);
		
		private final String key;
		private final int id;
		AlertHandlingConclusion(String key, int id) { 
			this.key = key;
			this.id = id;
		}
		
		public String getKey() { return key; }
		public int getId(){ return id; }
	}
	
	public static enum EventCode{ PANIC(7), FALL_DETECTION(82), APP_PANIC(103);
		 private final int id;
		 EventCode(int id) { this.id = id;}
		 public int getId() {return id;}
	}
	
	public static enum DeviceType{EPP(6), EPA(9);
		private final int id;
		
		DeviceType(int id) {this.id = id;}
		public int getId() {return id;}
		
		public static DeviceType getById(int id) {
		    for(DeviceType deviceType : values()) {
		        if(deviceType.getId() == id){
		        	return deviceType;
		        }
		    }
		    return null;
		 }
	}
	
	private boolean isLowActivity;
	private AlertState currentState;
	private AlertType type;
	private IssueType issuesType;
	private String[] photoURLs;
	private int alertSessionId;
	public String deviceNumber[] = new String[] {"N/A","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
	private Location location;
	private int eventCode;
	private int deviceType;
	private String mobileId; 
	

	private static int idGenerator = 99;
	
	/**
	 * Default Constructor
	 */
	public Alert(){	}
	
	/**
	 * Parameterized Constructor
	 * @param patient
	 * @param type
	 */
	public Alert(Patient patient, AlertType type){
		
		int id = idGenerator++;
		this.id = String.valueOf(id); // FIXME: Fake Id generator (for dummy only)
		logger.info("\nGenerating New Alert of type {} with id {}\n", type, this.id);

		this.type = type;
		
		// By default, the 'name' attribute is setted to the type in lower case,
		// but it can be changed, as in case of alerts of type 'USER'
		this.name = this.type.toString().toLowerCase();
		
		if (startDateTime == null){
			super.setStartDateTime(Calendar.getInstance().getTime());
		}else{
			super.setStartDateTime(startDateTime);
		}
		super.setPatient(patient);
		
	}

	
	
	
	public IssueType getIssuesType() {
		return issuesType;
	}

	public void setIssuesType(IssueType issuesType) {
		this.issuesType = issuesType;
	}

	public String[] getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String[] deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	@JsonIgnore
	public boolean isClosed(){
		return this.currentState == AlertState.CLOSED;
	}
	
	public AlertState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(AlertState currentState) {
		this.currentState = currentState;
	}

	public AlertType getType() {
		return type;
	}

	public void setType(AlertType type) {
		this.type = type;
	}

	public synchronized void close(){
		this.currentState = AlertState.CLOSED;
		this.setEndDateTime(Calendar.getInstance().getTime());
	}

	public void addAction(Action action) {
		super.addAction(action);
		this.currentState = AlertState.IN_PROGRESS;
	}
	
	@Override
	public String getName() {
		if (this.name == null)
			this.name = this.type.toString().toLowerCase();
		return super.getName();
	}

	public int getAlertSessionId() {
		return alertSessionId;
	}

	public void setAlertSessionId(int alertSessionId) {
		this.alertSessionId = alertSessionId;
	}

	public String[] getPhotoURLs() {
		return photoURLs;
	}

	public void setPhotoURLs(String[] photoURLs) {
		this.photoURLs = photoURLs;
	}

	public boolean isLowActivity() {
		return isLowActivity;
	}

	public void setLowActivity(boolean isLowActivity) {
		this.isLowActivity = isLowActivity;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getEventCode() {
		return eventCode;
	}

	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
}
