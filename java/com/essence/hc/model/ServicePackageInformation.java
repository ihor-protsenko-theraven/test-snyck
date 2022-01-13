package com.essence.hc.model;

import java.util.ArrayList;
import java.util.List;

public class ServicePackageInformation {

	/**
	 * The ID of the HSP service package (unique for all service packages across
	 * HSPs)
	 */
	private int id;

	/**
	 * The codename used to identify the service package, e.g.: “Family”,
	 * “Pro”, etc.
	 */
	private String codeName;

	/**
	 * The code of panels to which the service package may be assigned, e.g.: Family
	 * panels (code = 8) may be assigned “Family” or “PERS-E” service packages.
	 */
	private List<Integer> controlPanelServicePackages = new ArrayList<>();

	private List<ControlPanelAssociation> controlPanelAssociations = new ArrayList<>();

	/**
	 * Indicates whether the service package may be assigned to accounts (either new
	 * accounts or ones with a different service package)
	 */
	private boolean enabledForNewAccount;

	/**
	 * Service package ranking order
	 */
	private int ranking;
	/**
	 * Default emergency call settings for Care@Home Active for residents with this
	 * service package
	 */
	private DefaultActiveEmergencyCallSettings defaultActiveEmergencyCallSettings;
	
	private DefaultComplianceSettings defaultComplianceSettings;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public DefaultActiveEmergencyCallSettings getDefaultActiveEmergencyCallSettings() {
		return defaultActiveEmergencyCallSettings;
	}

	public void setDefaultActiveEmergencyCallSettings(
			DefaultActiveEmergencyCallSettings defaultActiveEmergencyCallSettings) {
		this.defaultActiveEmergencyCallSettings = defaultActiveEmergencyCallSettings;
	}

	public boolean isEnabledForNewAccount() {
		return enabledForNewAccount;
	}

	public int getRanking() {
		return ranking;
	}

	public void setEnabledForNewAccount(boolean enabledForNewAccount) {
		this.enabledForNewAccount = enabledForNewAccount;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public DefaultComplianceSettings getDefaultComplianceSettings() {
		return defaultComplianceSettings;
	}

	public void setDefaultComplianceSettings(DefaultComplianceSettings defaultComplianceSettings) {
		this.defaultComplianceSettings = defaultComplianceSettings;
	}

	public List<Integer> getControlPanelServicePackages() {
		return controlPanelServicePackages;
	}

	public void setControlPanelServicePackages(List<Integer> controlPanelServicePackages) {
		this.controlPanelServicePackages = controlPanelServicePackages;
	}

	public List<ControlPanelAssociation> getControlPanelAssociations() {
		return controlPanelAssociations;
	}

	public void setControlPanelAssociations(List<ControlPanelAssociation> controlPanelAssociations) {
		this.controlPanelAssociations = controlPanelAssociations;
	}
}
