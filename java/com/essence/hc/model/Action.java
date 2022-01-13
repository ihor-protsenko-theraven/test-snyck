package com.essence.hc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import com.essence.hc.service.PatientService;
import com.essence.hc.util.Util;
import com.essence.security.SecurityService;

/**
 * An Action is an operation taken by the user as a response 
 * to an activity or as a reaction to an alert, 
 * but not necessarily related to any of them.
 * 
 * @author oscar.canalejo
 */
//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="actionClass")  
//@JsonSubTypes({  
//    @Type(value=TakePhotoAction.class, name="take_photo") })
public abstract class Action implements Serializable, Command {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	PatientService patientService;
	@Autowired
	SecurityService securityService;
	
	
	protected Map<String,String> attributes;
	
	protected String id;
	protected String name;						// Convention name. Setted at applicationContext.xml
	protected String title;						// Name for the view components
	protected Date dateTime; 					// Creation Date/Time
	protected String authorComment; 			// Optional text note with user's obervations    
	
	protected String patientId;					

	protected String alertId;					// Alert which caused the action (alert session id).
	protected User author;						// User who took the action
	protected String parentId;					// Parent Action (in case this action is related to another)
	protected Event cause;						// Event (usually an alert) which caused the action.	
//	protected Action parent;
//	protected List<Event> relatedEvents;
	
//	protected String deviceId;					// Some actions require a device (i.e.: Take Photo) 
	
	public static int idGenerator = 999;
	

	/**
	 * Action Initialization
	 * initializes common action attributes
	 */
	public synchronized void init() {
		this.dateTime = Util.getCurrentDate();
		this.author = securityService.getPrincipal();
		this.patientId = String.valueOf(this.author.getCurrentPatient().getUserId());
		if (attributes != null && !attributes.isEmpty()) {
			this.alertId = attributes.get("sessionId");
			this.parentId = attributes.get("parentId");
		}
	}
	
//	/**
//	 * Action Initialization
//	 * <i>NOTE: The value for 'name' attribute is setted at bean definition 
//	 * in the corresponding xml configuration file</i> 
//	 */
//	public synchronized void init() {
//		int id = idGenerator++;
//		this.id = String.valueOf(id); // FIXME: Fake Id generator (for dummy only)
//		this.dateTime = Util.getCurrentDate();
////		this.title = util.getI18NMessage("action." + this.name);
//		this.patientId = securityService.getPrincipal().getCurrentPatient().getStringUserId();
//	}

	/**
	 * Gets data required by the action for being functional 
	 * @param context
	 * @throws Exception
	 */
	public synchronized void getData() {
		/*
		 *  Children classes can override this method to retrieve their own data
		 */
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.chain.Command#execute(org.apache.commons.chain.Context)
	 */
	public synchronized boolean execute(Context context) throws Exception {
		/*
		 * Business Rule: A closed alert cannot process actions
		 */
//		if (this.cause != null && this.cause instanceof Alert) {
//			Alert alert = (Alert) this.cause;
//			if (alert.isClosed()) {
//				throw new AppRuntimeException(util.getI18NMessage("error.alert_is_closed"));
//			}
//		}
		return true;
	}
	
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore 
//	 this is necessary to avoid the error 
//	"JsonMapingException: Infinite recursion (StackOverflowError)"
	public Event getCause() {
		return cause;
	}
	public void setCause(Event cause) {
		this.cause = cause;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getAuthorComment() {
		return authorComment;
	}

	public void setAuthorComment(String authorComment) {
		this.authorComment = authorComment;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

//	public String getDeviceId() {
//		return deviceId;
//	}
//
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}

	public String getAlertId() {
		return alertId;
	}

	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	
	
}
