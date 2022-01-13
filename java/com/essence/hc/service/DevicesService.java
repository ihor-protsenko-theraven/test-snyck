package com.essence.hc.service;

import org.springframework.stereotype.Service;

import com.essence.hc.model.ResponseStatus;

@Service
public interface DevicesService {
	
	/**
	 * sets device info by sending raw JSON data
	 * @param body body of the request
	 * @return
	 */
	public ResponseStatus setDeviceRawBody(Object body);
	
	/**
	* Remove device from account
	* @param account
	* @param deviceIdentifier
	* @return
	*/
	public ResponseStatus ExternalAPIDeviceRemoveFromAccount(String account, String deviceIdentifier);
	
}
