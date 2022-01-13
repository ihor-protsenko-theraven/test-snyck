package com.essence.hc.web.tags.single_svg;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SvgSingleActivityIconTag extends SimpleTagSupport {

	protected String iconType;
	protected String id;
	protected String cssClass;

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSvgPath() {

		// TODO ADL ROOTH PATH EXTERNAL ON JSP OR GET FROM SESSION VARIABLE OR
		// STH LIKE THAT
		String svgPath = "/ADL/stylesheets/svg/" + "activity-icons/";
		svgPath += this.obtainIconName(iconType) + ".svg";

		return svgPath;
	}

	private String obtainIconName(String iconType) {

		String iconName = null;

		if (iconType != null) {
			if (iconType.equalsIgnoreCase("BEDROOM") || iconType.equalsIgnoreCase("SLEEP")) {
				iconName = "BEDROOM_SENSOR";
			} else if (iconType.equalsIgnoreCase("BATHROOM")) {
				iconName = "BATHROOM_SENSOR";
			} else if (iconType.equalsIgnoreCase("DINING_ROOM") || iconType.equalsIgnoreCase("MEAL")) {
				iconName = "FRIDGE_DOOR";
			} else if (iconType.equalsIgnoreCase("RESTROOM") || iconType.equalsIgnoreCase("TOILET")) {
				iconName = "TOILET_ROOM_SENSOR";
			} else if (iconType.equalsIgnoreCase("LIVINGROOM")) {
				iconName = "LIVING_ROOM";
			} else if (iconType.equalsIgnoreCase("UNKNOWN")) {
				iconName = "OTHER_ROOM";
			} else if (iconType.equalsIgnoreCase("FRONTDOOR")) {
				iconName = "MAIN_DOOR";
			} else if (iconType.equalsIgnoreCase("")) {
				iconName = "UNKNOWN";
			} else {
				iconName = iconType;
			}
		} else {
			iconName = "UNKNOWN";
		}

		return iconName.toLowerCase();
	}

	@Override
	public void doTag() throws JspException, IOException {
		renderTag(getSvgPath());
	}

	public void renderTag(String svgPath) throws JspException, IOException {
		final JspWriter writer = getJspContext().getOut();

		writer.print("<img class='icon");
		if (cssClass != null)
			writer.print(" " + cssClass);
		writer.print("'");
		if (id != null)
			writer.print(" id='" + id + "'");
		if (svgPath != null)
			writer.print("src=\"" + svgPath + "\"");
		writer.print("/>");
	}

}
