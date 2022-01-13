/**
 * 
 */
package com.essence.hc.exceptions;

/**
 * @author oscar.canalejo
 *
 */
public class AppRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
  
	public AppRuntimeException(Throwable cause){
		super(cause);
	}
	public AppRuntimeException(Throwable cause,String msgKey){
		super(msgKey, cause);
	}
   
	public AppRuntimeException(String msgKey){
		super(msgKey);
	}

}
