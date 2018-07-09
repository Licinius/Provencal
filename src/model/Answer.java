package model;

import java.io.Serializable;
import java.util.Date;

public class Answer extends Post implements Serializable {
	
	private int parentId;

	//CONSTRUCTOR
	public Answer(int id, Date creationDate, Date deletionDate, int score, String body, Date lastEditDate,
			Date closedDate, Date communityOwnedDate, User owner, User lastEditor, int parentId) {
		super(id, creationDate, deletionDate, score, body, lastEditDate, closedDate, communityOwnedDate, owner, lastEditor);
		this.parentId = parentId;
	}

	//GETTER & SETTER
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

}
