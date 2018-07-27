package model;

import java.io.Serializable;
import java.util.Date;


/**
 * This class represent a postlink on StackOverflow
 * @author Crauser
 *
 */
@SuppressWarnings("serial")
public class PostLink implements Serializable {
	
	private int id;
	private Date date;
	private int linkTypeId;
	private Question question;
	private Question linkedQuestion;
	
	/**
	 * Class constructor with all attributes (null possible)
	 * @param id the postlink id
	 * @param date the date when the link has been create
	 * @param linkTypeId the type of link (1 : related, 3: linked) 
	 * @param question the first question linked
	 * @param linkedQuestion the second question linked
	 */
	public PostLink(int id, Date date, int linkTypeId, Question question, Question linkedQuestion) {
		super();
		this.id = id;
		this.date = date;
		this.linkTypeId = linkTypeId;
		this.question = question;
		this.linkedQuestion = linkedQuestion;
	}

	/**
	 * 
	 * @return the postlink id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id set a new id to the postlink
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return the date of the postlink
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * 
	 * @param date a new date for the postlink
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * 
	 * @return the type of the postlink
	 */
	public int getLinkTypeId() {
		return linkTypeId;
	}
	
	/**
	 * 
	 * @param linkTypeId set a new type for the postlink
	 */
	public void setLinkTypeId(int linkTypeId) {
		this.linkTypeId = linkTypeId;
	}

	/**
	 * 
	 * @return the first questions
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * 
	 * @param question is a new first question
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * 
	 * @return return the second question
	 */
	public Question getLinkedQuestion() {
		return linkedQuestion;
	}

	/**
	 * 
	 * @param linkedQuestion sets a new second question
	 */
	public void setLinkedQuestion(Question linkedQuestion) {
		this.linkedQuestion = linkedQuestion;
	}
		
}
