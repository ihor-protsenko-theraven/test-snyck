/**
 * 
 */
package com.essence.hc.eil;

/**
 * @author oscar.canalejo
 *
 */
public class Response {

//	public Response(Class<T> value) {
//	}

	private String responseCode; // 0=OK
//	private Class<T> value;


	public boolean hasError(){
		return Integer.parseInt(responseCode) <= 0; 
	}
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
 
}
