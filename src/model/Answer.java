package model;

import java.io.Serializable;
import java.util.Date;

/**
 * An answer of a Question is also a Post
 * {@link Post} 
 * @author Crauser
 *
 */
@SuppressWarnings("serial")
public class Answer extends Post implements Serializable {
	private int parentId;
	/**
	 * Create an answer with all the attributes (null possible)
	 * @param id The answer id
	 * @param creationDate the date when the answer has been created
	 * @param deletionDate the date when the answer has been deleted, null if the answer hasn't been deleted
	 * @param score The answer score on SO
	 * @param body The answer body 
	 * @param lastEditDate The last time the question has been edited
	 * @param closedDate The date when the answer has been closed (null if the answer is open)
	 * @param communityOwnedDate The date when the answer started to belong to the community
	 * @param owner The user that owns the answer
	 * @param lastEditor The last editor of the answer
	 * @param parentId The parent id, i.e the question answered
	 */
	public Answer(int id, Date creationDate, Date deletionDate, int score, String body, Date lastEditDate,
			Date closedDate, Date communityOwnedDate, User owner, User lastEditor, int parentId) {
		super(id, creationDate, deletionDate, score, body, lastEditDate, closedDate, communityOwnedDate, owner, lastEditor);
		this.parentId = parentId;
	}

	/**
	 * 
	 * @return The id of the question answered
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * 
	 * @param parentId A id of a question 
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}