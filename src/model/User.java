package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class User implements Serializable {
	
	private String displayName;

	/**
	 * 
	 * @param displayName The name displayed
	 */
	public User(String displayName) {
		super();
		this.displayName = displayName;
	}

	/**
	 * 
	 * @return the name displayed
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * 
	 * @param displayName change the user name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
