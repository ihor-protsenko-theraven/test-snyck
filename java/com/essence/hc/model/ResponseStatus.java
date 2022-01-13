/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ADL Response Status
 * State of whole set of components located at the patient's home, 
 * 
 * @author daniel.alcantarilla
 *
 */

public class ResponseStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String messageErr;
	private int numErr;
	private Boolean OK;
	private String value;
	private String message;
	
	
	@JsonIgnore
	public String getMessageErr() {
		String finalMessageErr;
		// For 167 error, MessageErr is a param returned in Message Field that will be shown in a popup
		// and needs translation (ex: User.firstName = First Name").
		// In order to organize the translations, in that case the app will add to the message "XSS" string
		if ((numErr == 167) || (numErr == -167) ) {
			finalMessageErr =  getErrMessageAsArgument();
		}
		else finalMessageErr = messageErr;
		
		return finalMessageErr;
	}
	
	public void setMessageErr(String messageErr) {
		this.messageErr = messageErr;
	}
	public int getNumErr() {
		return numErr;
	}
	public void setNumErr(int numErr) {
		this.numErr = numErr;
	}

	public Boolean isOK() {
		if ("OK".equals(messageErr))
			return true;
		if (OK != null)
			return OK;
		else
			return numErr >= 0;
	}

	public void setOK(Boolean OK) {
		this.OK = OK;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	// For error 167 cases, message error shows the first parameter (separated by ';') that
	// presents XSS Problems
	public String getErrMessageAsArgument()
	{
		String ret = "xss.";
		
		if (numErr == 167)
		{
			try {
				ret = ret + message.split(";")[0].toLowerCase();
			}
			catch (Exception e) {
				logger.error("Error 167 doesn´t provide Message information.\n");
			}
		}
		else if (numErr == -167)
		{
			ret = ret + messageErr;
		}
	
		return ret;
	}
	
	
}
