package com.essence.hc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SvgIconTagBase extends SimpleTagSupport {
	protected String baseSvgPath;
	protected String iconType;
	protected String id;
	protected String cssClass;
	
	public String getBaseSvgPath() {
		return baseSvgPath;
	}
	
	public void setBaseSvgPath(String baseSvgPath) {
		this.baseSvgPath = baseSvgPath;
	}
	
	public String getIconType() {
		return this.iconType;
	}
	
	public void setIconType(String type) {
		this.iconType = type;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCssClass() {
		return this.cssClass;
	}
	
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	public String getSvgPath() {
		return this.baseSvgPath + "#" + this.iconType;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		renderTag( getSvgPath() );
	}
	
	public void renderTag(String svgPath) throws JspException, IOException {
		final JspWriter writer = getJspContext().getOut();
		
		writer.print("<svg class='icon");
		if (cssClass != null) writer.print(" " + cssClass);
		writer.print("'");
		if (id != null) writer.print(" id='" + id + "'");
		writer.print(">");
		if (svgPath != null) writer.print("<use xlink:href=\"" + svgPath + "\" />");
		writer.print("</svg>");
	}
}
