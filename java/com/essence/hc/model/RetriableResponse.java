package com.essence.hc.model;

public class RetriableResponse<T> {
	
	
	private T response;
	private long retryTime;
	private boolean controlFlag;
	

	
	
	public long getRetryTime() {
		return retryTime;
	}
	public void setRetryTime(long retryTime) {
		this.retryTime = retryTime;
	}
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}
	public boolean getControlFlag() {
		return controlFlag;
	}
	public void setControlFlag(boolean controlFlag) {
		this.controlFlag = controlFlag;
	}

}
