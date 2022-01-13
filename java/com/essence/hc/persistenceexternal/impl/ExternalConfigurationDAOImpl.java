package com.essence.hc.persistenceexternal.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.essence.hc.model.TimeZone;
import com.essence.hc.model.VendorConfigurationSettings;
import com.essence.hc.persistence.impl.ExternalDAOImpl;
import com.essence.hc.persistenceexternal.ExternalConfigurationDAO;

public class ExternalConfigurationDAOImpl extends ExternalDAOImpl implements ExternalConfigurationDAO 
{
	@Override
	public VendorConfigurationSettings getVendorConfigurationSettings() {
		
		return performRequest(VendorConfigurationSettings.class, "external_vendor_config", null);
	}

	@Override
	public List<TimeZone> getTimeZones(int languageId) {
		
		HashMap<String, Object> reqParams = new HashMap<String,Object>();
		
		reqParams.put("languageId", languageId);
		
		List<TimeZone> timeZones = performRequest(ArrayList.class, "external_get_time_zones", reqParams);
		return timeZones;
	}
}
