package model;

import java.io.Serializable;
import java.util.Date;

public class Vote implements Serializable {
	
	private int id;
	private int voteTypeId;
	private Date creationDate;
	private int bountyAmount;
	
	private Post post;
	private User user;
	
	//CONSTRUCTOR
	public Vote(int id, int voteTypeId, Date creationDate, int bountyAmount, Post post, User user) {
		super();
		this.id = id;
		this.voteTypeId = voteTypeId;
		this.creationDate = creationDate;
		this.bountyAmount = bountyAmount;
		this.post = post;
		this.user = user;
	}	
	
	//GETTER & SETTER
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVoteTypeId() {
		return voteTypeId;
	}

	public void setVoteTypeId(int voteTypeId) {
		this.voteTypeId = voteTypeId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getBountyAmount() {
		return bountyAmount;
	}

	public void setBountyAmount(int bountyAmount) {
		this.bountyAmount = bountyAmount;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
