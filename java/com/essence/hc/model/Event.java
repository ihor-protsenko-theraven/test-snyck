package com.essence.hc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.springframework.util.StringUtils;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.essence.hc.util.Util;

/**
 *	This superclass provides an abstraction for handling activities 
 *  and alerts in a single common way when, for instance, we need 
 *  to treat them as “occurrences which took place at a specific date and
 *	time, which are representable in a time line”.
 * 
 * @author oscar.canalejo
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "eventClass")  
@JsonSubTypes({  
    @Type(value = Activity.class, name = "activity"),
    @Type(value = Alert.class, name = "alert") })
public abstract class Event  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Event Types Enumeration
	 */
	public static enum EventType {
		ACTIVITY("Activity"), SOS("Sos"), TECHNICAL("Technical"), INFORMATION("Information");
		
		private final String name;
		
		EventType(String name) { this.name = name; }
		
		public String getName() { return name; }
		
		public String toString() { return getName(); }
		
		public static EventType getEventType(String text) {
			if (StringUtils.hasText(text)) {
				for (EventType et : EventType.values()) {
					if (text.equalsIgnoreCase(et.name)) {
						return et;
					}
				}
			}
			return null;
		}
	}
	
	protected String id;
	protected String name;					// Convention name. Setted at applicationContext.xml
	protected String title;					// Name for the view components
	protected Date startDateTime;
	protected Date endDateTime;
	
	protected Patient patient;
	protected List<Action> takenActions = new ArrayList<Action>();
	protected List<Event> relatedEvents = new ArrayList<Event>();
	

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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Date getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public List<Action> getTakenActions() {
		return takenActions;
	}
	public void setTakenActions(List<Action> takenActions) {
		this.takenActions = takenActions;
	}
	public List<Event> getRelatedEvents() {
		return relatedEvents;
	}
	public void setRelatedEvents(List<Event> relatedEvents) {
		this.relatedEvents = relatedEvents;
	}
	
	
	@JsonIgnore
	public void addAction(Action action) {
		this.takenActions.add(action);
	}
	
	@JsonIgnore
	public String getStartTime(){
		return Util.getTime(this.startDateTime);
	}
	
	@JsonIgnore
	public String getEndTime(){
		return Util.getTime(this.endDateTime);
	}
	
	public String getFormattedStartDateTime(){
		return Util.formatFullDate(this.startDateTime);
	}
	
	public String getFormattedEndDateTime(){
		return Util.formatFullDate(this.endDateTime);
	}

}
