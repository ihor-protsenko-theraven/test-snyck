package com.essence.hc.exceptions;

public class CaregiverAssignmentException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message;

	public CaregiverAssignmentException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
