package model;

import java.io.Serializable;
import java.util.Date;

public class PostLink implements Serializable {
	
	private int id;
	private Date date;
	private int linkTypeId;
	private Question question;
	private Question linkedQuestion;
	
	//CONSTUCTOR
	public PostLink(int id, Date date, int linkTypeId, Question question, Question linkedQuestion) {
		super();
		this.id = id;
		this.date = date;
		this.linkTypeId = linkTypeId;
		this.question = question;
		this.linkedQuestion = linkedQuestion;
	}

	//GETTER & SETTER
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getLinkTypeId() {
		return linkTypeId;
	}

	public void setLinkTypeId(int linkTypeId) {
		this.linkTypeId = linkTypeId;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Question getLinkedQuestion() {
		return linkedQuestion;
	}

	public void setLinkedQuestion(Question linkedQuestion) {
		this.linkedQuestion = linkedQuestion;
	}
		
}
