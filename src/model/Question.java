package model;

import java.io.Serializable;
import java.util.Collection;
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
	
	
	/**
	 * A class constructor for all attribute (null possible)
	 * @param id The question id
	 * @param creationDate the date when the question has been created
	 * @param deletionDate the date when the question has been deleted, null if the question hasn't been deleted
	 * @param score The question score on SO
	 * @param body The question body 
	 * @param lastEditDate The last time the question has been edited
	 * @param closedDate The date when the question has been closed (null if the answer is open)
	 * @param communityOwnedDate The date when the question started to belong to the community
	 * @param owner The user that owns the question
	 * @param lastEditor The last editor of the question
	 * @param title The title of the question
	 * @param favoriteCount the score of favorite 
	 * @param viewCount the number of view
	 * @param tags all the tags in the question
	 */
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
	
	/**
	 * Class constructor to create a dummy question
	 */
	public Question() {
		super();
		this.title = "Question Title";
		this.classes = new Classes();
		this.mapAnswer = new HashMap<Integer, Answer>();
	}

	/**
	 * 
	 * @return the question title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title set a new title for the question
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 
	 * @return the favorite count
	 */
	public int getFavoriteCount() {
		return favoriteCount;
	}

	/**
	 * 
	 * @param favoriteCount update the favoriteCount
	 */
	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	/**
	 * 
	 * @return the viewCount
	 */
	public int getViewCount() {
		return viewCount;
	}

	/**
	 * 
	 * @param viewCount update the view count
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * 
	 * @return all the question tags 
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * 
	 * @param tags sets new tags
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	/**
	 * 
	 * @return the Map of answers
	 */
	public HashMap<Integer, Answer> getMapAnswer() {
		return mapAnswer;
	}

	/**
	 * 
	 * @param mapAnswer sets a new mapAnswer
	 */
	public void setMapAnswer(HashMap<Integer, Answer> mapAnswer) {
		this.mapAnswer = mapAnswer;
	}

	/**
	 * 
	 * @return the selected answer by the user
	 */
	public Answer getSelectedAnswer() {
		return selectedAnswer;
	}

	/**
	 * 
	 * @param selectedAnswer changes the selected answer
	 */
	public void setSelectedAnswer(Answer selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}
	
	/**
	 * 
	 * @return all the class 
	 */
	public Classes getClasses() {
		return this.classes;
	}
	
	/**
	 * Add a answer to the question
	 * @param a the answer to add
	 */
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
	 * @param aClass is a class to add to the question classes 
	 * @return True if the class added to the classes
	 */
	public boolean addClass(Class aClass) {
		return classes.add(aClass);
	}
	/**
	 * Remove a class to the classes
	 * @param aClass is a class to remove to the question classes
	 * @return True if the class is correctly remove in the classes
	 */
	public boolean removeClass(Class aClass) {
		return classes.remove(aClass);
	}
	
	/**
	 * Clear the class {@link Collection#clear}
	 */
	public void clearClasses() {
		classes.clear();
	}
	
	/**
	 * 
	 * @return the size of the map of Answer
	 */
	public int getAnswerCount() {
		return this.mapAnswer.size();
	}
	
}