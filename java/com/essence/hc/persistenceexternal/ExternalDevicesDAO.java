package com.essence.hc.persistenceexternal;

import com.essence.hc.model.ResponseStatus;

public interface ExternalDevicesDAO 
{
		/**
		 * Updates the information for a device
		 * @param body
		 * @return
		 */
		 public ResponseStatus updateDevice(Object body);
		 
		 /**
		* Remove device from account
		* @param account
		* @param deviceIdentifier
		* @return
		*/
		 public ResponseStatus ExternalAPIDeviceRemoveFromAccount(String account, String deviceIdentifier);
}
