package com.essence.hc.eil.parsers;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ResponseStatus;


public class MessageStatusPrsr implements IParser<ResponseStatus> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String message; 	
	private int id;
	
	public MessageStatusPrsr() {
	}


	@Override
	public ResponseStatus parse() {
//		logger.info("\nParsing************************ " + "...\n");
		ResponseStatus response = new ResponseStatus();
		try {	
			response.setMessageErr(message);
			response.setNumErr(id);			
			
		}catch(Exception ex) {
			throw new ParseException(ex,"Unexpected parse error");
		}	
		
		return response;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}




	

}
