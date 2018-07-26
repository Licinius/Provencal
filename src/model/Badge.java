package model;

import java.io.Serializable;
import java.util.Date;

/**
 * The badge on StackOverflow are reward for the user
 * @author Crauser
 *
 */
@SuppressWarnings("serial")
public class Badge implements Serializable {
	
	private int id;
	private String name;
	private Date date;
	private int classBadge;
	private boolean tagBased;
	private int userId;
	
	/**
	 * Create a badge with all the attributes (null possible)
	 * @param id The badge id
	 * @param name The name of the badge
	 * @param date The date on which the user obtained the badge 
	 * @param classBadge The class of the badge
	 * @param tagBased The badge tag
	 * @param userId The user id that owns the badge
	 */
	public Badge(int id, String name, Date date, int classBadge, boolean tagBased, int userId) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		this.classBadge = classBadge;
		this.tagBased = tagBased;
		this.userId = userId;
	}

	/**
	 * 
	 * @return An integer, the id of the badge
	 */
	public int getId() {
		return id;
	}
	/**
	 * 
	 * @param id a integer that will be the id of badge
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return a string, the name of the badge (ex : Socratic,...)
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name a new name for the badge
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return the date when the user earned the badge
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * 
	 * @param date A Date to sets
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * 
	 * @return an integer representing the badge class
	 */
	public int getClassBadge() {
		return classBadge;
	}
	
	/**
	 * 
	 * @param classBadge A new integer for the badge class
	 */
	public void setClassBadge(int classBadge) {
		this.classBadge = classBadge;
	}

	/**
	 * 
	 * @return true if the badge is tag based
	 */
	public boolean isTagBased() {
		return tagBased;
	}
	
	/**
	 * 
	 * @param tagBased set if whether or not the badge is tag based
	 */
	public void setTagBased(boolean tagBased) {
		this.tagBased = tagBased;
	}
	
	/**
	 * 
	 * @return an integer of the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId, set a new user for the badge
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}