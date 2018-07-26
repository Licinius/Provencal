package model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents the comments on StackOverflow
 * @author Crauser
 *
 */
@SuppressWarnings("serial")
public class Comment implements Serializable {
	
	private int id;
	private int score;
	private String text;
	private Date creationDate;
	private int postId;
	
	
	/**
	 * Create a comment with all the attributes (null possible)
	 * @param id the comment id
	 * @param score the score of the comment
	 * @param text the text in the comment
	 * @param creationDate the date when the comment has been posted
	 * @param postId the post related to the comment
	 */
	public Comment(int id, int score, String text, Date creationDate, int postId) {
		super();
		this.id = id;
		this.score = score;
		this.text = text;
		this.creationDate = creationDate;
		this.postId = postId;
	}

	/**
	 * 
	 * @return the id of the comment
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id to set the comment id 
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return the comment score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * 
	 * @param score A new score for the comment
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * 
	 * @return the text in the comment
	 */
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text will be the new body of the comment
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 
	 * @return the creation date of the comment
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * 
	 * @param creationDate set a new date of creation
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * 
	 * @return the id of the related post
	 */
	public int getPostId() {
		return postId;
	}
	
	/**
	 * 
	 * @param postId the id of the related post
	 */
	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	
}
