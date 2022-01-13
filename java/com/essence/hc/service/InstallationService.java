package com.essence.hc.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.essence.hc.model.Activity.ActivityType;
import com.essence.hc.model.Device;
import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.InstallationDevices;
import com.essence.hc.model.Panel;
import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;

/**
 * @author daniel.alcantarilla
 *
 */
@Service
public interface InstallationService {

	@PreAuthorize("isAuthenticated()")
	public Set<ActivityType> getInstallableDevices(Patient patient);

	@PreAuthorize("isAuthenticated()")
	public boolean hasCorrelatedDevOpt(Patient patient);

	@PreAuthorize("isAuthenticated()")
	public ResponseStatus DeviceAddByUserId(int userId, int deviceType, int activityTypeId, int deviceId, String label,
			boolean isIPD, int relatedDevID); // , int ErrorMessageId);

	@PreAuthorize("isAuthenticated()")
	public ResponseStatus DeviceUpdateByUserId(int userId, int deviceType, int activityTypeId, int deviceId,
			String label, boolean isIPD, int ErrorMessageId, int OriginalDeviceId, int oldDeviceType, int relatedDevID);

	@PreAuthorize("isAuthenticated()")
	public ResponseStatus ExternalAPIDeviceUpdateByUserId(int userId, int deviceType, int deviceFamily, int deviceId,
			int activityTypeId, String label, boolean isIPD, int ErrorMessageId, String deviceIdentifier,
			int oldDeviceType, String relatedDevIdentifier);

	@PreAuthorize("isAuthenticated()")
	public ResponseStatus DeviceDeleteByUserId(int userId, int deviceType, int deviceId);

	@PreAuthorize("isAuthenticated()")
	public InstallationDevices DeviceListByUserId(String userId);

	@PreAuthorize("isAuthenticated()")
	public Device GetDevice(int deviceId);

	@PreAuthorize("isAuthenticated()")
	public List<Panel> GetFreeAccounts();

	@PreAuthorize("isAuthenticated()")
	public ExternalAPIInstallationDevices DeviceListByExternalAPI(String account, String userId);

}
