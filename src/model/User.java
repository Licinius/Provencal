package model;

import java.io.Serializable;

public abstract class User implements Serializable {
	
	private String displayName;

	//CONSTRUCTOR
	public User(String displayName) {
		super();
		this.displayName = displayName;
	}

	//GETTER & SETTER
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
