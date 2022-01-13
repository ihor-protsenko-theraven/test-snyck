package com.essence.hc.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class PasswordPolicy {

	/**
	 * Minimum char number accepted for the password
	 */
	private int minimumLength = 6;
	/**
	 * Maximum char number accepted for the password
	 */
	private int maximumLength = 20;

	/**
	 * Does password must have at least x out of 4 character groups
	 */
	private boolean charGroupsActive = true;

	/**
	 * Accepted values 1,2,3,4
	 */
	private int charGroupsMinimum = 3;

	private boolean historyActive = true;

	private int numOfHistoryGenerations = 3;
	
	private int passwordExpirationDays = 365;

	@JsonProperty("isHistoryActive")
	public boolean isHistoryActive() {
		return historyActive;
	}

	@JsonProperty("isHistoryActive")
	public void setIsHistoryActive(boolean isActive) {
		historyActive = isActive;
	}

	@JsonProperty("numOfHistoryGenerations")
	public int getNumOfHistoryGenerations() {
		return numOfHistoryGenerations;
	}

	@JsonProperty("numOfHistoryGenerations")
	public void setNumOfHistoryGenerations(int numOfGenerations) {
		this.numOfHistoryGenerations = numOfGenerations;
	}

	@JsonProperty("passwordMinimumLength")
	public int getMinimumLength() {
		return minimumLength;
	}

	@JsonProperty("passwordMinimumLength")
	public void setMinimumLength(int minimumLength) {
		this.minimumLength = minimumLength;
	}

	@JsonProperty("passwordMaximumLength")
	public int getMaximumLength() {
		return maximumLength;
	}

	@JsonProperty("passwordMaximumLength")
	public void setMaximumLength(int maximumLength) {
		this.maximumLength = maximumLength;
	}

	@JsonProperty("charGroupsActive")
	public boolean isCharGroupsActive() {
		return charGroupsActive;
	}

	@JsonProperty("charGroupsActive")
	public void setCharGroupsActive(boolean charGroupsActive) {
		this.charGroupsActive = charGroupsActive;
	}

	@JsonProperty("charGroupsMinimum")
	public int getCharGroupsMinimum() {
		return charGroupsMinimum;
	}

	@JsonProperty("charGroupsMinimum")
	public void setCharGroupsMinimum(int charGroupsMinimum) {
		this.charGroupsMinimum = charGroupsMinimum;
	}

	@JsonProperty("passwordExpirationDays")
	public int getPasswordExpirationDays() {
		return passwordExpirationDays;
	}
	@JsonProperty("passwordExpirationDays")
	public void setPasswordExpirationDays(int passwordExpirationDays) {
		this.passwordExpirationDays = passwordExpirationDays;
	}
}
