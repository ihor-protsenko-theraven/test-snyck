package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.AlertCloseReason;

public class AlertCloseReasonPrsr implements IParser<AlertCloseReason> {
	
	private boolean descriptionMandatory;
	private String text;
	private int code;
	private String name;
	
	public AlertCloseReasonPrsr() {		
	}
	
	@Override
	public AlertCloseReason parse() {
		AlertCloseReason reason = new AlertCloseReason();
		
		try{	
			reason.setCode(code);
			reason.setName(name);
			reason.setText(text);
			reason.setDescriptionMandatory(descriptionMandatory);
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}
		return reason;
	}

	/**
	 * @return the descriptionMandatory
	 */
	@JsonProperty("DescriptionMandatory")
	public boolean isDescriptionMandatory() {
		return descriptionMandatory;
	}

	/**
	 * @param descriptionMandatory the descriptionMandatory to set
	 */
	@JsonProperty("DescriptionMandatory")
	public void setDescriptionMandatory(boolean descriptionMandatory) {
		this.descriptionMandatory = descriptionMandatory;
	}

	/**
	 * @return the text
	 */
	@JsonProperty("Text")
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	@JsonProperty("Text")
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the code
	 */
	@JsonProperty("Code")
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	@JsonProperty("Code")
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}
	
}
