package com.essence.hc.web.tags;

import com.essence.hc.web.tags.SvgIconTagBase;

public class SvgActivityIconTag extends SvgIconTagBase {
	
	@Override
	public String getSvgPath() {
		String svgPath = baseSvgPath; 
		
		if ( cssClass == null ) setCssClass("icon-activity");
		
		if ( iconType != null  ) {
			if ( iconType.equalsIgnoreCase("BEDROOM") ||
				 iconType.equalsIgnoreCase("SLEEP") ) {
				svgPath += "#BEDROOM_SENSOR";
			} else if ( iconType.equalsIgnoreCase("BATHROOM") ) {
				svgPath += "#BATHROOM_SENSOR";
			} else if ( iconType.equalsIgnoreCase("DINING_ROOM") ||
				 iconType.equalsIgnoreCase("MEAL") ) {
				svgPath += "#FRIDGE_DOOR";
			} else if ( iconType.equalsIgnoreCase("RESTROOM") ||
						iconType.equalsIgnoreCase("TOILET") ) {
				svgPath += "#TOILET_ROOM_SENSOR";
			} else if ( iconType.equalsIgnoreCase("LIVINGROOM") ) {
				svgPath += "#LIVING_ROOM";
			} else if ( iconType.equalsIgnoreCase("UNKNOWN") ) {
				svgPath += "#OTHER_ROOM";
			} else if ( iconType.equalsIgnoreCase("FRONTDOOR") ) {
				svgPath += "#MAIN_DOOR";
			} else {
				svgPath += "#" + iconType.toUpperCase();
			}
		}
		return svgPath;
	}
}
