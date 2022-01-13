package com.essence.hc.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.essence.hc.model.Action;

@Service
public interface ActionService {

//	@PreAuthorize("isAuthenticated()")
//	public List<HCCommand> getCommands();
	
//	@PreAuthorize("isAuthenticated()")
//	public boolean takePhoto(Context context);
	
	@PreAuthorize("isAuthenticated()")
	public boolean execute(Action action);
}
