package com.essence.hc.persistenceexternal;

import java.util.List;
import java.util.Map;

import com.essence.hc.model.TimeZone;
import com.essence.hc.model.User.CaregiverType;
import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.model.VendorConfigurationSettings;

public interface ExternalConfigurationDAO 
{
	/**
	 * returns a map with the vendor configuration settings
	 * @return
	 */
	public VendorConfigurationSettings getVendorConfigurationSettings();
	
	/**
	 * returns time zones enabled for the vendor and the default time zone for the vendor
	 * @return
	 */
	public List<TimeZone> getTimeZones(int languageId);
}
