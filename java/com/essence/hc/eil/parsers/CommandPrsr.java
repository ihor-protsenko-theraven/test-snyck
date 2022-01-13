package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.eil.exceptions.ParseException;
import com.essence.hc.model.HCCommand;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandPrsr implements IParser<HCCommand> {

	
	private int id;				// database identifier. Security is applied to this id
	private String name;       	// Normalized Command name for identification. Image ssociated
		// Visible name, for the view components

	
	public CommandPrsr() {
	}

	@Override
	public HCCommand parse() {
		HCCommand command = new HCCommand();
		try {			
				command.setId(id);
				command.setName(name);
			
				
			}catch(Exception ex) {
				throw new ParseException(ex,"Unexpected parse error");
			}
		return command;
	}

	@JsonProperty("Id")
	public int getId() {
		return id;
	}
	@JsonProperty("Id")
	public void setId(int id) {
		this.id = id;
	}
	@JsonProperty("Name")
	public String getName() {
		return name;
	}
	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}
	
}
