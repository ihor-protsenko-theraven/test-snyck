package com.essence.hc.eil.parsers;

import org.codehaus.jackson.annotate.JsonProperty;

import com.essence.hc.model.Vendor.ServiceType;
import com.essence.hc.util.Util;

public class ServicetTypePrsr implements IParser<ServiceType> {

//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String name;
	
	@Override
	public ServiceType parse() {
		return ServiceType.fromString(Util.mapFromNewToOldServicePackageNaming(this.name)); 
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
