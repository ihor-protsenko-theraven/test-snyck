package com.essence.hc.model;

/**
 * Contains a validation error for a SetAccontRules request
 *
 */
public class RuleValidationError {
	
	/**
	 * Entity being validated
	 */
	private String validatedEntity;
	
	/**
	 * Category that is not fulfilled
	 */
	private String validationCategory;
	
	private String validationSubCategory;
	
	/**
	 * Error description
	 */
	private String message;

	public String getValidatedEntity() {
		return validatedEntity;
	}

	public void setValidatedEntity(String validatedEntity) {
		this.validatedEntity = validatedEntity;
	}

	public String getValidationCategory() {
		return validationCategory;
	}

	public void setValidationCategory(String validationCategory) {
		this.validationCategory = validationCategory;
	}

	public String getValidationSubCategory() {
		return validationSubCategory;
	}

	public void setValidationSubCategory(String validationSubCategory) {
		this.validationSubCategory = validationSubCategory;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
