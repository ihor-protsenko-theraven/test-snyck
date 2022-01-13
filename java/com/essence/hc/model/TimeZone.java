package com.essence.hc.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 
 * 
 * ADL Time Zone
 * Represents a time zone
 * 
 * @author adrian.casado
 *
 */
public class TimeZone implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String countryCode;
	private int timeZoneId;
	private String olsonName;
	private String name;
	private boolean isDefault;
	
	public static Comparator<TimeZone> timeZoneComparator = new Comparator<TimeZone>(){
		
		public int compare(TimeZone tz1, TimeZone tz2){
			
			String tzName1 = tz1.getName().toUpperCase();
			String tzName2 = tz2.getName().toUpperCase();
			
			return tzName1.compareTo(tzName2);
			
		}
	};
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public int getTimeZoneId() {
		return timeZoneId;
	}
	
	public void setTimeZoneId(int timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	
	public String getOlsonName() {
		return olsonName;
	}
	
	public void setOlsonName(String olsonName) {
		this.olsonName = olsonName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	

}
