package com.essence.hc.web.tags.single_svg;

public class SvgSingleLocationIconTag extends SvgSingleTagBase {
	
	@Override
	public String getCssClass() {
		String cssClass = "icon-location";
		if ( this.cssClass != null ) cssClass += " " + this.cssClass;
		
		return cssClass;
	}
	
	@Override
	protected String obtainIconName(String iconType) {
		return iconType.toLowerCase();
	}
	
	@Override
	protected String getIconFolder() {
		return "location-icons/";
	}

}
