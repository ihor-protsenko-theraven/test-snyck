package com.essence.hc.model;

/**	
 * Class use to respond ajax json request in desktop app
 * it has title, message and success for notify popup
 * and response for inject object of response 
 *  
 * @author Samuel Santiago
 *
 * @param <T>
 */
public class FlashJSONResponse<T> {
	private String title;
	private String message;
	private T response;
	private boolean success;
	
	public FlashJSONResponse(String title, String message, T response, boolean success) {
		this.title    = title;
		this.message  = message;
		this.response = response;
		this.success  = success;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setMessage(String message) {
		this.message= message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setResponse(T response) {
		this.response = response;
	}
	
	public T getResponse() {
		return this.response;
	}
	
	public void setSuccess(boolean success) {
		this.success= success;
	}
	
	public boolean getSuccess() {
		return this.success;
	}
}
