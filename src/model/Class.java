package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class of a questions with a name and keywords
 * @author Florent
 *
 */
public class Class implements Serializable {
	
	/**
	 * Generated SerialVersionUID
	 */
	private static final long serialVersionUID = 2132483397083171462L;
	private String  name;
	private ArrayList<String> keywords;
	private ArrayList<Question> questions;
	private int sizeQuestions;
	
	public Class(String name,ArrayList<String> keywords) {
		this.name = name;
		this.keywords = keywords;
		this.questions = new ArrayList<Question>();
		this.sizeQuestions = 0;
	}
	public Class(String name) {
		this.name = name;
		this.keywords = new ArrayList<>();
		this.questions = new ArrayList<Question>();
		this.sizeQuestions = 0;
	}
	public Class() {}
	
	public void addQuestion(Question question) {
		questions.add(question);
		sizeQuestions = questions.size();
	}
	public void removeQuestion(Question question) {
		questions.remove(question);
		sizeQuestions = questions.size();
	}

	
	public int getSizeQuestions() {
		return sizeQuestions;
	}
	
	public boolean contains(String keyword) {
		return keywords.contains(keyword);
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<String> getKeywords(){
		return keywords;
	}
	
	public ArrayList<Question> getQuestions(){
		return questions;
	}
}
