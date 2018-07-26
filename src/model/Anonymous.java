package model;

import java.io.Serializable;
@SuppressWarnings("serial")
public class Anonymous extends User implements Serializable {

	public Anonymous(String displayName) {
		super(displayName);
	}
	
}
