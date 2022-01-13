package com.essence.hc.eil.parsers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.HCCommand;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandListPrsr implements IParser<Map<String, HCCommand>> {

	private List<CommandPrsr> data;
	
	public CommandListPrsr() {
	}

	@Override
	public Map<String, HCCommand> parse() {
		Map<String, HCCommand> commands = null;
		try { 
			if(data != null)		 
				commands = new LinkedHashMap<String, HCCommand>();
				for(CommandPrsr c: data){
					commands.put(c.getName(), (HCCommand) c.parse());
				}
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return  commands;
	}

	@JsonProperty("Data")
	public List<CommandPrsr> getData() {
		return data;
	}
	
	@JsonProperty("Data")
	public void setData(List<CommandPrsr> data) {
		this.data = data;
	}

	



}
