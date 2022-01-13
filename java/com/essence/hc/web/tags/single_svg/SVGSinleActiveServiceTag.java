package com.essence.hc.web.tags.single_svg;

public class SVGSinleActiveServiceTag extends SvgSingleTagBase {
	
	@Override
	public String getCssClass() {
		String cssClass = "icon-epa-active";
		if ( this.cssClass != null ) cssClass += " " + this.cssClass;
		
		return cssClass;
	}
	
	@Override
	protected String obtainIconName(String iconType) {
		return "epa-active-icon";
	}
}
