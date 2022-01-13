package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Device;
import com.essence.hc.model.InstallationDevices;

public class ListDevicesPrsr implements IParser<InstallationDevices> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	List<DevicePrsr> deviceList;
	boolean isSynchronized;
	
	public ListDevicesPrsr() {
	}


	@Override
	public InstallationDevices parse() {
//		logger.info("\nParsing " + getClass() + "...\n");
		List<Device> devices = new ArrayList <Device>();
		List<Device> devicesWithCorrelated = new ArrayList <Device>();
		try { 
			if(deviceList != null)		 
				for(DevicePrsr d: deviceList){
					Device dev = (Device) d.parse(); 
					devices.add(dev);
					if (dev.getCorrelatedDevID() > 0) {
						devicesWithCorrelated.add(dev);
					}
				}
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
			// set the correlated devices
			for(Device dev: devicesWithCorrelated){
				for (Device d : devices) {
					if(d.getInstallationID() == dev.getCorrelatedDevID()) {
						dev.setCorrelatedDev(d);
						break;
					}
				}
			}
		InstallationDevices iDevices = new InstallationDevices();
		iDevices.setIsSyncronized(isSynchronized);
		iDevices.setDevicesList(devices);
		
		return  iDevices;
	}


	public List<DevicePrsr> getDeviceList() {
		return deviceList;
	}


	public void setDeviceList(List<DevicePrsr> deviceList) {
		this.deviceList = deviceList;
	}
	
	@JsonProperty("isSynchronized")
	public boolean getIsSynchronized(){
		return this.isSynchronized;
	}
	
	@JsonProperty("isSynchronized")
	public void setIsSynchronized(boolean isSynchronized){
		this.isSynchronized = isSynchronized;
	}
	

	
	
}
