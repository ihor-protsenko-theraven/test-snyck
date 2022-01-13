package com.essence.hc.eil.parsers;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ResponseStatus;


public class ResponseStatusPrsr implements IParser<ResponseStatus> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String sErrorStr; 	
	private int iErr;
	
	public ResponseStatusPrsr() {
	}


	@Override
	public ResponseStatus parse() {
//		logger.info("\nParsing************************ " + "...\n");
		ResponseStatus response = new ResponseStatus();
		try {	
			response.setMessageErr(sErrorStr);
			response.setNumErr(iErr);			
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}	
		
		return response;
	}

	public int getiErr() {
		return iErr;
	}


	public void setiErr(int iErr) {
		this.iErr = iErr;
	}


	public String getsErrorStr() {
		return sErrorStr;
	}


	public void setsErrorStr(String sErrorStr) {
		this.sErrorStr = sErrorStr;
	}


	

}
