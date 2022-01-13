package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.Device;

public class ListCamerasPrsr implements IParser<List<Device>> {
	
	List<CameraPrsr> sCameraList;
	
	public ListCamerasPrsr() {
	}


	@Override
	public List<Device> parse() {

		List<Device> devices = new ArrayList <Device>();
		
		try { 
			if(sCameraList != null)		 
				for(CameraPrsr i: sCameraList){
					devices.add((Device) i.parse());
				}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}

		return  devices;
	}

	@JsonProperty("Cameras")
	public List<CameraPrsr> getCameraList() {
		return sCameraList;
	}

	@JsonProperty("Cameras")
	public void setCameraList(List<CameraPrsr> cameraList) {
		sCameraList = cameraList;
	}


	
	
}
