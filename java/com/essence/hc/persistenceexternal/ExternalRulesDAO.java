package com.essence.hc.persistenceexternal;

import com.essence.hc.model.ResponseStatus;
import com.essence.hc.model.Vendor.ServiceType;

public interface ExternalRulesDAO 
{
	 /**
	 * returns rules policy for a given ServiceType
	 * @return
	 */
	 public Object getRulesPolicy(ServiceType serviceType);
	 
	 /**
	 * returns account rules
	 * @param panel
	 * @return
	 */
	public Object getAccountRules(String account, ServiceType st);
	
	 /**
	 * Sets account rules by sending JSON data in the body of the request
	 * @param body JSON body for the request
	 * @return
	 */
	 public ResponseStatus setAccountRulesRawBody(Object body);
}
