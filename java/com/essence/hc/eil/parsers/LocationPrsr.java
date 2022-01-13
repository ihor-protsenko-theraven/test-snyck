package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.dozer.DozerBeanMapper;

import com.essence.hc.model.Location;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationPrsr  implements IParser<Location>{

	private float longitude;
	private float latitude;
	private float horizontalAccuracy;
	
	@Override
	public Location parse() {
		
		DozerBeanMapper mapper = new DozerBeanMapper();
		return mapper.map(this, Location.class);
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getHorizontalAccuracy() {
		return horizontalAccuracy;
	}

	public void setHorizontalAccuracy(float horizontalAccuracy) {
		this.horizontalAccuracy = horizontalAccuracy;
	}
	
}
