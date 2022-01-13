/**
 * 
 */
package com.essence.hc.eil.exceptions;

import com.essence.hc.exceptions.AppRuntimeException;

/**
 * @author oscar.canalejo
 *
 */
public class ParseException extends AppRuntimeException {

	private static final long serialVersionUID = 1L;

	public ParseException(Throwable cause) {
		super(cause);
	}

	public ParseException(Throwable cause, String msgKey) {
		super(cause, msgKey);
	}

	public ParseException(String msgKey) {
		super(msgKey);
	}

}
