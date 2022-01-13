package com.essence.hc.persistenceexternal.impl;

import java.util.HashMap;

import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.RuleValidationErrorResponseStatus;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalDevicesDAO;

public class ExternalDevicesDAOImpl extends ExternalDAOImpl implements ExternalDevicesDAO {
	
	
	public ResponseStatus updateDevice(Object body) {
		ResponseStatus rs = performRequestRawBody(ResponseStatus.class, "external_update_device", body);
		if (rs.getNumErr() == 76) {
			rs = new RuleValidationErrorResponseStatus(rs);
		}
		return rs;
	}
	
	@Override
	public ResponseStatus ExternalAPIDeviceRemoveFromAccount(String account, String deviceIdentifier) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("account", account);
		reqParams.put("deviceIdentifier", deviceIdentifier);

		ResponseStatus response = performRequest(ResponseStatus.class, "external_remove_device_from_account", reqParams);

		return response;
	}
}
