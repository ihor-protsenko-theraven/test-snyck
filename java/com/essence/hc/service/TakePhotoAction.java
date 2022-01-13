/**
 * 
 */
package com.essence.hc.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.model.Action;
import com.essence.hc.model.Device;
import com.essence.hc.model.ResponseStatus;

/**
 * @author oscar.canalejo
 *
 */
@Service
public class TakePhotoAction extends Action {

	private static final long serialVersionUID = 1L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("requestProcessor")
	RequestProcessor reqProcessor;
	@Autowired
	PatientService patientService;
	
	
	private List<Device> cameraList;
	/*
	 * Attributes
	 */
	private String deviceId;					// photo device chosen for taking the photos
	

	@Override
	public synchronized void init() {
		super.init();
		if (attributes != null && !attributes.isEmpty()) {
			this.deviceId = attributes.get("deviceId");
		}
	}

	@Override
	public synchronized void getData() {
		logger.info("\n\n Executing getData {} \n\n", getPatientId());
		this.cameraList = patientService.getCameraList(getPatientId());
	}
	
	@Override
	public synchronized boolean execute(Context context)  throws Exception {
		
		logger.info("\nExecuting Action 'Take Photo'... for camera device {}\n", this.deviceId);
		super.execute(context);


		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("caregiverId", this.author.getId());	
		reqParams.put("residentId", this.patientId);
		reqParams.put("deviceId", this.deviceId);		
		reqParams.put("alertSessionId", this.alertId);
		ResponseStatus resp = reqProcessor.performRequest(ResponseStatus.class, "request_photo", reqParams);
		
		return resp.getNumErr() >= 0;
	}

	
	
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public List<Device> getCameraList() {
		return cameraList;
	}
	public void setCameraList(List<Device> cameraList) {
		this.cameraList = cameraList;
	}

}
