package com.essence.hc.eil.parsers;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Device;


public class CameraPrsr implements IParser<Device> {
	
	private String deviceid;
	private String label;
	
	public CameraPrsr() {
	}


	@Override
	public Device parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		
		Device device = new Device();
		
		try{
			
			device.setId(deviceid);
			device.setLabel(label);
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return device;
	}


	public String getDeviceid() {
		return deviceid;
	}


	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

		
}
