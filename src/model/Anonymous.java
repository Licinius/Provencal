package model;

import java.io.Serializable;

/**
 * Sub Class of user to represent an anonymous user.
 * On StackOverflow an anonymous user doesn't have a profil.
 * @author Crauser
 */
@SuppressWarnings("serial")
public class Anonymous extends User implements Serializable {
	/**
	 * 
	 * @param displayName The name displayed
	 */
	public Anonymous(String displayName) {
		super(displayName);
	}
}