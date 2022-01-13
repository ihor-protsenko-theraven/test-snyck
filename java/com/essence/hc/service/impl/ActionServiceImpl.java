/**
 * 
 */
package com.essence.hc.service.impl;

import org.apache.commons.chain.impl.ContextBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essence.hc.exceptions.AppRuntimeException;
import com.essence.hc.model.Action;
import com.essence.hc.service.ActionService;

/**
 * @author oscar.canalejo
 *
 */
public class ActionServiceImpl implements ActionService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	
	/* (non-Javadoc)
	 * @see com.essence.hc.service.ActionService#takeAction(org.apache.commons.chain.Context)
	 */
	@Override
	public boolean execute(Action action) {
		
		try{
			return action.execute(new ContextBase());
			
		}catch(Exception ex){
//			ex.printStackTrace();
			throw new AppRuntimeException(ex);
		}
			
	}

}
