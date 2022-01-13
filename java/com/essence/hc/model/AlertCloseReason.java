/**
 * 
 */
package com.essence.hc.model;

import java.io.Serializable;

/**
 * A reason given for closing an alert
 * @author ruben.sanchez
 *
 */
public class AlertCloseReason implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int code;
	public String name;
	public String text;
	public boolean descriptionMandatory;
	
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the descriptionMandatory
	 */
	public boolean isDescriptionMandatory() {
		return descriptionMandatory;
	}
	/**
	 * @param descriptionMandatory the descriptionMandatory to set
	 */
	public void setDescriptionMandatory(boolean descriptionMandatory) {
		this.descriptionMandatory = descriptionMandatory;
	}
	
	

}
