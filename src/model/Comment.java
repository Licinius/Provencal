package model;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
	
	private int id;
	private int score;
	private String text;
	private Date creationDate;
	private int postId;
	
	
	//CONSTRUCTOR
	public Comment(int id, int score, String text, Date creationDate, int postId) {
		super();
		this.id = id;
		this.score = score;
		this.text = text;
		this.creationDate = creationDate;
		this.postId = postId;
	}

	//GETTER & SETTER
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public int getPostId() {
		return postId;
	}
	
	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	
}
