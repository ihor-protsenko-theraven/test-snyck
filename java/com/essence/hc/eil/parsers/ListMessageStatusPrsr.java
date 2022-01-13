package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.ResponseStatus;


public class ListMessageStatusPrsr implements IParser<List<ResponseStatus>> {
	
	List<MessageStatusPrsr> messageList;
	
	public ListMessageStatusPrsr() {	}

	@Override
	public List<ResponseStatus> parse() {
		List<ResponseStatus> status = new ArrayList <ResponseStatus>();
		
		try { 
			if(messageList!=null)		 
				for(MessageStatusPrsr i: messageList){
					status.add((ResponseStatus) i.parse());
				}
			
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return status;
	}


	public List<MessageStatusPrsr> getMessageList() {
		return messageList;
	}


	public void setMessageList(List<MessageStatusPrsr> messageList) {
		this.messageList = messageList;
	}


	
	
	

	
	
}
