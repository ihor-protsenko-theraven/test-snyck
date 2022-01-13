package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.essence.hc.model.TimeZone;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalAPITimeZonesPrsr implements IParser<List<TimeZone>> {
	
	private ExternalAPITimeZonePrsr defaultTimeZone;
	private List<ExternalAPITimeZonePrsr> timeZones;

	@Override
	public List<TimeZone> parse() {
		
		List<TimeZone> timeZones = new ArrayList<TimeZone>();
		TimeZone defaultTimeZone = new TimeZone();
		
		if (this.defaultTimeZone != null){
			defaultTimeZone = this.defaultTimeZone.parse();
		}
		
		if(this.timeZones != null){
			for (ExternalAPITimeZonePrsr externalAPITimeZonePrsr : this.timeZones) {
				
				TimeZone timeZone = (TimeZone)externalAPITimeZonePrsr.parse();
				
				if(timeZone.getTimeZoneId() == defaultTimeZone.getTimeZoneId()){
					timeZone.setDefault(true);
				}else{
					timeZone.setDefault(false);
				}
				
				timeZones.add(timeZone);
			}
			
			Collections.sort(timeZones, TimeZone.timeZoneComparator);
		}
		
		return timeZones;
	}

	public ExternalAPITimeZonePrsr getDefaultTimeZone() {
		return defaultTimeZone;
	}

	public void setDefaultTimeZone(ExternalAPITimeZonePrsr defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}

	public List<ExternalAPITimeZonePrsr> getTimeZones() {
		return timeZones;
	}

	public void setTimeZones(List<ExternalAPITimeZonePrsr> timeZones) {
		this.timeZones = timeZones;
	}

	
}
