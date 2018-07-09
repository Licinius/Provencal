package model;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public abstract class Post implements Serializable {
	
	private int id;
	private Date creationDate;
	private Date deletionDate;
	private int score;
	private String body;
	private Date lastEditDate;
	private Date closedDate;
	private Date communityOwnedDate;
	
	private HashMap<Integer,Comment> mapComment;
	private User owner;
	private User lastEditor;
	
	
	//CONSTRUCTOR
	public Post(int id, Date creationDate, Date deletionDate, int score, String body, Date lastEditDate, Date closedDate,
			Date communityOwnedDate, User owner, User lastEditor) {
		super();
		this.id = id;
		this.creationDate = creationDate;
		this.deletionDate = deletionDate;
		this.score = score;
		this.body = body;
		this.lastEditDate = lastEditDate;
		this.closedDate = closedDate;
		this.communityOwnedDate = communityOwnedDate;
		this.mapComment = new HashMap<Integer,Comment>();
		this.owner = owner;
		this.lastEditor = lastEditor;
	}
	
	public Post() {
		this.body="Le corps d'une question, le corps d'une question peut contenir des mots clefs interréssants";
		this.id =  (int) (Math.random() * 10000 ); ;
	}
	//GETTER & SETTER
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(Date deletionDate) {
		this.deletionDate = deletionDate;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getLastEditDate() {
		return lastEditDate;
	}

	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public Date getCommunityOwnedDate() {
		return communityOwnedDate;
	}

	public void setCommunityOwnedDate(Date communityOwnedDate) {
		this.communityOwnedDate = communityOwnedDate;
	}

	public HashMap<Integer,Comment> getMapComment() {
		return mapComment;
	}

	public void setMapComment(HashMap<Integer,Comment> mapComment) {
		this.mapComment = mapComment;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getLastEditor() {
		return lastEditor;
	}

	public void setLastEditor(User lastEditor) {
		this.lastEditor = lastEditor;
	}

	//FUNTION
	public void addComment(Comment c) {
		this.mapComment.put(c.getId(), c);
	}
	
	public int getCommentCount() {
		return this.mapComment.size();
	}
	public String getUrl() {
		return "https://stackoverflow.com/questions/"+id;
	}
}