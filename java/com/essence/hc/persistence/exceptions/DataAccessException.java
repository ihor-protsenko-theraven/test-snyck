/**
 * 
 */
package com.essence.hc.persistence.exceptions;

import com.essence.hc.exceptions.AppRuntimeException;

/**
 * @author oscar.canalejo
 *
 */
public class DataAccessException extends AppRuntimeException {

	private static final long serialVersionUID = 1L;

	public DataAccessException(String msgKey) {
		super(msgKey);
	}

	public DataAccessException(Throwable cause, String msgKey) {
		super(cause, msgKey);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}
	
}
