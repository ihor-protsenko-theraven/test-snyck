package com.essence.hc.persistence.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.essence.hc.eil.requestprocessors.RequestProcessor;
import com.essence.hc.eil.requestprocessors.impl.DummyRequestProcessor;
import com.essence.hc.model.Device;
import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.InstallationDevices;
import com.essence.hc.model.Panel;
import com.essence.hc.model.ResponseStatus;
import com.essence.hc.persistence.InstallationDAO;
import com.essence.hc.persistenceexternal.ExternalAccountDAO;
import com.essence.hc.util.Util;

/**
 * @author daniel.alcantarilla
 *
 */
public class InstallationDAOImpl implements InstallationDAO {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("requestProcessor")
	RequestProcessor reqProcessor;

	@Autowired
	private ExternalAccountDAO externalAccountDao;

	@Override
	public Device GetDevice(int deviceId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("id", String.valueOf(deviceId));
		Device device = new Device();
		device = reqProcessor.performRequest(Device.class, "get_device", reqParams);
		return device;
	}

	@Override
	public ExternalAPIInstallationDevices DeviceListByExternalAPI(String account) {
		return externalAccountDao.getExternalAPIInstallationDevices(account);
	}

	@Override
	public InstallationDevices DeviceListByUserId(String userId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userID", userId);

		// TODO: remove me to production
		if (Util.isDebug()) {
			DummyRequestProcessor reqProcessor = new DummyRequestProcessor();
			return reqProcessor.performRequest(InstallationDevices.class, "list_devices", reqParams);
		}

		return reqProcessor.performRequest(InstallationDevices.class, "list_devices", reqParams);
	}

	@Override
	public List<Panel> GetFreeAccounts() {
		return reqProcessor.performRequest(ArrayList.class, "free_accounts", null);
	}

	@Override
	public ResponseStatus DeviceAddByUserId(int userId, int deviceType, int activityTypeId, int deviceId, String label,
			boolean isIPD, int relatedDevID) { // , int ErrorMessageId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userID", String.valueOf(userId));
		reqParams.put("devFamily", String.valueOf(deviceType));
		reqParams.put("ActivityTypeID", String.valueOf(activityTypeId));
		reqParams.put("DeviceID", String.valueOf(deviceId));
		reqParams.put("label", (label == null || label.isEmpty()) ? "null" : label);
		reqParams.put("IsIPD", String.valueOf(isIPD));
		reqParams.put("ErrorMessageID", "0");
		if (relatedDevID > 0)
			reqParams.put("iDevRelated", String.valueOf(relatedDevID));

		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "add_device", reqParams);

		return response;
	}

	@Override
	public ResponseStatus DeviceUpdateByUserId(int userId, int deviceType, int activityTypeId, int deviceId,
			String label, boolean isIPD, int ErrorMessageId, int OriginalDeviceId, int oldDeviceType,
			int relatedDevID) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userID", String.valueOf(userId));
		reqParams.put("devFamily", String.valueOf(deviceType));
		// reqParams.put("OriginalFamilyId", String.valueOf(oldDeviceType));
		reqParams.put("ActivityTypeID", String.valueOf(activityTypeId));
		reqParams.put("DeviceID", String.valueOf(deviceId));
		reqParams.put("label", (label == null || label.isEmpty()) ? "null" : label);
		reqParams.put("IsIPD", String.valueOf(isIPD));
		reqParams.put("ErrorMessageID", String.valueOf(ErrorMessageId));
		reqParams.put("OriginalDeviceId", String.valueOf(OriginalDeviceId));
		if (relatedDevID > 0)
			reqParams.put("iDevRelated", String.valueOf(relatedDevID));

		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "update_device", reqParams);

		return response;
	}

	public ResponseStatus ExternalAPIDeviceUpdateByUserId(int userId, int deviceType, int deviceFamily, int deviceId,
			int activityTypeId, String label, boolean isIPD, int ErrorMessageId, String deviceIdentifier,
			int oldDeviceType, String relatedDevIdentifier) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userID", String.valueOf(userId));
		reqParams.put("devFamily", String.valueOf(deviceFamily));
		reqParams.put("deviceID", String.valueOf(deviceId));
		reqParams.put("ActivityTypeID", String.valueOf(activityTypeId));
		reqParams.put("deviceIdentifier", deviceIdentifier);
		reqParams.put("label", (label == null || label.isEmpty()) ? "null" : label);
		reqParams.put("IsIPD", String.valueOf(isIPD));
		reqParams.put("ErrorMessageID", String.valueOf(ErrorMessageId));

		if ((relatedDevIdentifier != null) && (relatedDevIdentifier.length() > 0))
			reqParams.put("relatedDeviceIdentifier", relatedDevIdentifier);

		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "update_device", reqParams);

		return response;
	}

	@Override
	public ResponseStatus DeviceDeleteByUserId(int userId, int deviceType, int deviceId) {
		HashMap<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("userID", String.valueOf(userId));
		reqParams.put("deviceID", String.valueOf(deviceId));
		reqParams.put("devFamily", String.valueOf(deviceType));

		ResponseStatus response = reqProcessor.performRequest(ResponseStatus.class, "delete_device", reqParams);

		return response;
	}

}
