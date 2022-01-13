package com.essence.hc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import com.essence.hc.model.User.CaregiverType;

/**
 * The Customer/Service Provider which offers and manages the product for the
 * end user
 * 
 * @author oscar.canalejo
 *
 */
public class Vendor implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cmsPhone;
	private String logo;
	private String name;
	private String policePhone;
	private String timeZoneOffset;
	private ServiceType[] serviceTypes;
	private VendorConfigurationSettings configSettings;
	private List<TimeZone> timeZones;
	
	/**
	 * Service Types Enumeration
	 */
	public static enum ServiceType {
		ANALYTICS("Analytics"), EXPRESS("Express"), PERSE("PERS-E"), HELP_ANYWHERE("Umbrella");
		private final String name;

		ServiceType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return getName();
		}

		public static ServiceType fromString(String text) {
			if (StringUtils.hasText(text)) {
				if (text.equals("PERS-E"))
					return ServiceType.PERSE;
				
				if (text.equals("Umbrella"))
					return ServiceType.HELP_ANYWHERE;

				for (ServiceType st : ServiceType.values()) {

					if (!st.equals(ServiceType.PERSE) && !st.equals(ServiceType.HELP_ANYWHERE)  && text.equalsIgnoreCase(st.name)) {
						return st;
					}
				}
			}
			return null;
		}
	}

	public String getCmsPhone() {
		return cmsPhone;
	}

	public void setCmsPhone(String cmsPhone) {
		this.cmsPhone = cmsPhone;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPolicePhone() {
		return policePhone;
	}

	public void setPolicePhone(String policePhone) {
		this.policePhone = policePhone;
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public ServiceType[] getServiceTypes() {
		return serviceTypes;
	}

	public ServiceType[] getServiceTypesExcludingHelpAnywhere() {
		// Exclude MPERS if present. To be used in the adminNewUser.
		return ArrayUtils.removeElement(this.serviceTypes.clone(), ServiceType.HELP_ANYWHERE);
	}

	public void setServiceTypes(ServiceType[] serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public List<TimeZone> getTimeZones() {
		return timeZones;
	}

	public void setTimeZones(List<TimeZone> timeZones) {
		this.timeZones = timeZones;
	}

	/**
	 * Returns the maximum number of caregivers allowed in this vendor per service
	 * type and caregiver type
	 * 
	 * @param serviceType
	 * @param caregiverType
	 * @return
	 */
	public int getMaxCaregivers(ServiceType serviceType, CaregiverType caregiverType) {
		try {
			return this.configSettings.getMaxCaregiversByServiceTypeAndCaregiverType(serviceType, caregiverType);
		} catch (Exception e) {
			return -1;
		}
	}

	public VendorConfigurationSettings getConfigSettings() {
		return configSettings;
	}

	public void setConfigSettings(VendorConfigurationSettings configSettings) {
		this.configSettings = configSettings;
	}

	public String[] getPanelTypes() {
		List<ServicePackageInformation> servicePackages = this.getConfigSettings().getServicePackages();
		Set<String> panelTypesAll = new HashSet<>();
		for (ServicePackageInformation servicePackage : servicePackages) {
			if (servicePackage.isEnabledForNewAccount()) {
				for (ControlPanelAssociation controlPanelAssociation : servicePackage.getControlPanelAssociations()) {
					String panelType = controlPanelAssociation.getPanelDeviceType();
					panelTypesAll.add(panelType);
				}
			}
		}
		
		List<String> panelTypesList = new ArrayList<String>();
		// place in specific order
		if (panelTypesAll.contains("CP")) {
			panelTypesList.add("CP");
			panelTypesAll.remove("CP");
		}
		if (panelTypesAll.contains("C7000")) {
			panelTypesList.add("C7000");
			panelTypesAll.remove("C7000");
		}
		panelTypesList.addAll(panelTypesAll);		
		
		return panelTypesList.toArray(new String[panelTypesList.size()]);
	}
}
