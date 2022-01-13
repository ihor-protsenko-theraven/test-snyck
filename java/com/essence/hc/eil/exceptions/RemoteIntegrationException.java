/**
 * 
 */
package com.essence.hc.eil.exceptions;

import com.essence.hc.exceptions.AppRuntimeException;

/**
 * @author oscar.canalejo
 *
 */
public class RemoteIntegrationException extends AppRuntimeException {

	private static final long serialVersionUID = 1L;

	public RemoteIntegrationException(Throwable cause) {
		super(cause);
	}

	public RemoteIntegrationException(Throwable cause, String msgKey) {
		super(cause, msgKey);
	}

	public RemoteIntegrationException(String msgKey) {
		super(msgKey);
	}

}
