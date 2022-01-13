package com.essence.hc.persistenceexternal;

import com.essence.hc.model.Patient;
import com.essence.hc.model.ResponseStatus;

public interface ExternalAlertDAO 
{
	public ResponseStatus createManualAlert (Patient resident, String iAlrtType, String sAlertTxt, String deviceId);
	/**
	 * Closes an event
	 * @param accountNumber
	 * @param closeReasonCode
	 * @param closeReasonName
	 * @param handlingConclussionName
	 * @param sessionId
	 * @return
	 */
	public ResponseStatus closeAlertSession(int accountNumber, int closeReasonCode, String closeReasonName, String handlingConclusionName, String handlingDescription, int sessionId);
}
