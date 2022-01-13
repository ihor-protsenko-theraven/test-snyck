package com.essence.hc.eil.parsers;

import com.essence.hc.model.TimeZone;

public class ExternalAPITimeZonePrsr implements IParser<TimeZone>{

	private String countryCode;
	private int timeZoneId;
	private String olsonName;
	private String name;
	
	@Override
	public TimeZone parse() {
		
		TimeZone timeZone = new TimeZone();
		
		timeZone.setCountryCode(countryCode);
		timeZone.setTimeZoneId(timeZoneId);
		timeZone.setOlsonName(olsonName);
		timeZone.setName(name);
		
		return timeZone;
	}

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
	
	

}
