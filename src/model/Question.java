package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Question extends Post implements Serializable {
	private String title;
	private int favoriteCount;
	private int viewCount;
	private String[] tags;
	private Classes classes;
	private HashMap<Integer,Answer> mapAnswer;
	private Answer selectedAnswer;
	
	//CONSTRUCTOR
	public Question(int id, Date creationDate, Date deletionDate, int score, String body, Date lastEditDate,
			Date closedDate, Date communityOwnedDate, User owner, User lastEditor, String title, int favoriteCount,
			int viewCount, String[] tags) {
		super(id, creationDate, deletionDate, score, body, lastEditDate, closedDate, communityOwnedDate, owner,
				lastEditor);
		this.title = title;
		this.favoriteCount = favoriteCount;
		this.viewCount = viewCount;
		this.tags = tags;
		this.classes = new Classes();
		this.mapAnswer = new HashMap<Integer, Answer>();
	}
	public Question() {
		super();
		this.title = "Le titre d'une question";
		this.classes = new Classes();
		this.mapAnswer = new HashMap<Integer, Answer>();
	}

	//GETTER & SETTER
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public HashMap<Integer, Answer> getMapAnswer() {
		return mapAnswer;
	}

	public void setMapAnswer(HashMap<Integer, Answer> mapAnswer) {
		this.mapAnswer = mapAnswer;
	}

	public Answer getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(Answer selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}
	
	public Classes getClasses() {
		return this.classes;
	}
	
	//FUNCTION
	public void addAnswer (Answer a) {
		try {
			this.mapAnswer.put(a.getId(), a);
		} catch (NullPointerException e) {
			System.err.println("Post "+a.getId()+" | "+ a.toString());
		}
	}
	/**
	 * If the question has no classes then it's not classified
	 * @return True if the classes is not empty
	 */
	public boolean isClassified() {
		return !classes.isEmpty();
	}
	/**
	 * Add a class to the classes
	 * @param aClass
	 */
	public boolean addClass(Class aClass) {
		return classes.add(aClass);
	}
	/**
	 * Remove a class to the classes
	 * @param aClass
	 */
	public boolean removeClass(Class aClass) {
		return classes.remove(aClass);
	}
	
	public void clearClasses() {
		classes.clear();
	}
	public int getAnswerCount() {
		return this.mapAnswer.size();
	}
	
}