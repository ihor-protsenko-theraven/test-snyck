package com.essence.hc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.essence.hc.model.ResponseStatus;
import com.essence.hc.persistenceexternal.ExternalDevicesDAO;
import com.essence.hc.service.DevicesService;

public class DevicesServiceImpl implements DevicesService {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExternalDevicesDAO externalDevicesDao;

	@Override
	public ResponseStatus setDeviceRawBody(Object body) {
		return externalDevicesDao.updateDevice(body);
	}

	@Override
	public ResponseStatus ExternalAPIDeviceRemoveFromAccount(String account, String deviceIdentifier) {
		return externalDevicesDao.ExternalAPIDeviceRemoveFromAccount(account, deviceIdentifier);
	}
}
