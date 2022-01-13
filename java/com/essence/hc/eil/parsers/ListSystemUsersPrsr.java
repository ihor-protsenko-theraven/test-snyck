package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.SystemUser;


public class ListSystemUsersPrsr implements IParser<List<SystemUser>> {



//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 
	ArrayList<SystemUserPrsr> userInfoList;
	
	

	
	public ListSystemUsersPrsr() {
	}


	@Override
	public List<SystemUser> parse() {
		List<SystemUser> listSystemUsers = new ArrayList <SystemUser>();
			
		try {	
			
			if(userInfoList!=null)		 
				for(SystemUserPrsr i: userInfoList){
					listSystemUsers.add((SystemUser) i.parse());
				}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return listSystemUsers;
	}

	
	public ArrayList<SystemUserPrsr> getUserInfoList() {
		return userInfoList;
	}

	
	public void setUserInfoList(ArrayList<SystemUserPrsr> userInfoList) {
		this.userInfoList = userInfoList;
	}


	


	
	
}
