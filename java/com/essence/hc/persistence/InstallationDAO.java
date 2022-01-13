/**
 * 
 */
package com.essence.hc.persistence;

import java.util.List;

import com.essence.hc.model.Device;
import com.essence.hc.model.ExternalAPIInstallationDevices;
import com.essence.hc.model.InstallationDevices;
import com.essence.hc.model.Panel;
import com.essence.hc.model.ResponseStatus;

/**
 * @author daniel.alcantarilla
 *
 */
public interface InstallationDAO {

	public InstallationDevices DeviceListByUserId(String userId);

	public ResponseStatus DeviceAddByUserId(int userId, int deviceType, int activityTypeId, int deviceId, String label,
			boolean isIPD, int relatedDevID);// , int ErrorMessageId);

	public ResponseStatus DeviceUpdateByUserId(int userId, int deviceType, int activityTypeId, int deviceId,
			String label, boolean isIPD, int ErrorMessageId, int OriginalDeviceId, int oldDeviceType, int relatedDevID);

	public ResponseStatus ExternalAPIDeviceUpdateByUserId(int userId, int deviceType, int deviceFamily, int deviceId,
			int activityTypeId, String label, boolean isIPD, int ErrorMessageId, String deviceIdentifier,
			int oldDeviceType, String relatedDevIdentifier);

	public ResponseStatus DeviceDeleteByUserId(int userId, int deviceType, int deviceId);

	public Device GetDevice(int deviceId);

	public ExternalAPIInstallationDevices DeviceListByExternalAPI(String account);

	public List<Panel> GetFreeAccounts();

}
