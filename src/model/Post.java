package model;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;


/**
 * This abstract class can represent a post on StackOverflow (Questions &amp; Answer)
 * @author Crauser
 *
 */
@SuppressWarnings("serial")
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
	
	
	/**
	 * Create a post with all the attributes (null possible)
	 * @param id The post id
	 * @param creationDate the date when the post has been created
	 * @param deletionDate the date when the post has been deleted, null if the post hasn't been deleted
	 * @param score The post score on SO
	 * @param body The post body 
	 * @param lastEditDate The last time the post has been edited
	 * @param closedDate The date when the post has been closed (null if the answer is open)
	 * @param communityOwnedDate The date when the post started to belong to the community
	 * @param owner The user that owns the post
	 * @param lastEditor The last editor of the post
	 **/
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
	
	/**
	 * Empty constructor, initialize the body with a lorem ipsum and the id with a random integer
	 */
	public Post() {
		this.body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt "
				+ "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullam"
				+ "co laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit"
				+ " in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat"
				+ " cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ";
		this.id =  (int) (Math.random() * 10000 ); ;
	}
	
	/**
	 * 
	 * @return An integer, the id of the post
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id, a new integer for the post
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return the post creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * 
	 * @param creationDate a new post creation date
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * 
	 * @return the deletion date
	 */
	public Date getDeletionDate() {
		return deletionDate;
	}

	/**
	 * 
	 * @param deletionDate the post deletion date
	 */
	public void setDeletionDate(Date deletionDate) {
		this.deletionDate = deletionDate;
	}

	/**
	 * 
	 * @return the score of the post
	 */
	public int getScore() {
		return score;
	}

	/**
	 * 
	 * @param score to sets 
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * 
	 * @return the message of the post
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * 
	 * @param body a string to set the body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * 
	 * @return the last time the post has been edited
	 */
	public Date getLastEditDate() {
		return lastEditDate;
	}
	
	/**
	 * 
	 * @param lastEditDate a date of edition
	 */
	public void setLastEditDate(Date lastEditDate) {
		this.lastEditDate = lastEditDate;
	}

	/**
	 * 
	 * @return the date when the post has been closed
	 */
	public Date getClosedDate() {
		return closedDate;
	}

	/**
	 * 
	 * @param closedDate the date when the post closed
	 */
	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	/**
	 * 
	 * @return the community owned date
	 */
	public Date getCommunityOwnedDate() {
		return communityOwnedDate;
	}
	
	/**
	 * 
	 * @param communityOwnedDate the date when the community owned the post
	 */
	public void setCommunityOwnedDate(Date communityOwnedDate) {
		this.communityOwnedDate = communityOwnedDate;
	}
	
	/**
	 * 
	 * @return the map of the comment
	 */
	public HashMap<Integer,Comment> getMapComment() {
		return mapComment;
	}

	/**
	 * 
	 * @param mapComment to set the comments
	 */
	public void setMapComment(HashMap<Integer,Comment> mapComment) {
		this.mapComment = mapComment;
	}
	
	/**
	 * 
	 * @return the owner of the post
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param owner sets a new user that will own the post
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return the last editor of the post
	 */
	public User getLastEditor() {
		return lastEditor;
	}
	
	/**
	 * 
	 * @param lastEditor to edit the post
	 */
	public void setLastEditor(User lastEditor) {
		this.lastEditor = lastEditor;
	}

	/**
	 * 
	 * @param c a comment to add in the map of comment
	 */
	public void addComment(Comment c) {
		this.mapComment.put(c.getId(), c);
	}
	
	/**
	 * 
	 * @return the size of the comment map
	 */
	public int getCommentCount() {
		return this.mapComment.size();
	}
	
	/**
	 * 
	 * @return the url on the website stackOverflow 
	 */
	public String getUrl() {
		return "https://stackoverflow.com/questions/"+id;
	}
}