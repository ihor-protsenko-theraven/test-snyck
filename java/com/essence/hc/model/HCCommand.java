package com.essence.hc.model;

/**
 * This is a command that can extend Command pattern
 * @author manuel.garcia
 *
 */

public class HCCommand {
	
	private int id;			// database identifier. Security is applied to this id
	private String name;       // Normalized Command name for identification. Image ssociated
	private String title;		// Visible name, for the view components
	private String labelI18n;  // localized name... maybe unnecesary 

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabelI18n() {
		return labelI18n;
	}
	public void setLabelI18n(String labelI18n) {
		this.labelI18n = labelI18n;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
