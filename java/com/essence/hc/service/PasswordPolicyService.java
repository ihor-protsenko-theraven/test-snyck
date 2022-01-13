package com.essence.hc.service;

import org.springframework.stereotype.Service;

import com.essence.hc.model.PasswordPolicy;

@Service
public interface PasswordPolicyService {

	/**
	 * unauthenticated request in order to recover the password
	 * 
	 * @param username
	 *            - the user name requesting the password resert
	 */
	public void resetPassword(String username);

	/**
	 * Unauthenticated request to set a new password when the currentpassword is
	 * expired
	 * 
	 * @param username
	 * @param currentPassword
	 *            - current expired password
	 * @param newPassword
	 */
	public void changeExpiredPassword(String username, String currentPassword, String newPassword);

	/**
	 * Authenticated request in order to let the authenticated user to change its
	 * own password
	 * 
	 * @param currentPassword
	 *            - current working password
	 * @param newPassword
	 *            - desired password
	 */
	public void changeMyPassword(String currentPassword, String newPassword);

	/**
	 * Authenticated request in order to let the admin to set other user s password
	 * 
	 * @param username
	 *            - user whose pwd is going to change
	 * @param newPassword
	 *            - desired password
	 */
	public void changeUserPassword(String username, String newPassword);

	/**
	 * Retrieves the password policy for the given user type
	 * 
	 * @param userType
	 * @return
	 */
	public PasswordPolicy getPasswordPolicy();

	/**
	 * Will set the user with the current Eula version in the DB
	 */
	public void acceptEula(String version);

}
