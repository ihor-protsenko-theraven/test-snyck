package com.essence.hc.model;

public class Eula {
	
	/**
	 * Acceptance date for the eula version provided
	 */
	private String acceptanceDate;
	
	/**
	 * Eula version of the HSP
	 */
	private String version;
	
	/**
	 * Eula link configured for the HSP
	 */
	private String link;
	
	public String getAcceptanceDate() {
		return acceptanceDate;
	}
	public void setAcceptanceDate(String acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

}
