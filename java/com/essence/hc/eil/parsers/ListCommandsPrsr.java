package com.essence.hc.eil.parsers;

import java.util.ArrayList;
import java.util.List;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.HCCommand;


public class ListCommandsPrsr implements IParser<List<HCCommand>> {
	
	List<CommandPrsr> CommandList;
	
	public ListCommandsPrsr() {
	}


	@Override
	public List<HCCommand> parse() {

		List<HCCommand> commands = new ArrayList <HCCommand>();
		
		try { 
			if(CommandList != null)		 
				for(CommandPrsr i: CommandList){
					commands.add((HCCommand) i.parse());
				}
			
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}

		return commands;
	}


	public List<CommandPrsr> getCommandList() {
		return CommandList;
	}


	public void setCommandList(List<CommandPrsr> commandList) {
		CommandList = commandList;
	}

	
}
