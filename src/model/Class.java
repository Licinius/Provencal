package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class of a questions with a name and keywords
 * @author Dell'omo
 *
 */
public class Class implements Serializable {
	
	/**
	 * Generated SerialVersionUID
	 */
	private static final long serialVersionUID = 2132483397083171462L;
	private String  name;
	private ArrayList<Question> questions;
	
	public Class(String name,ArrayList<String> keywords) {
		this.name = name;
		this.questions = new ArrayList<Question>();
	}
	public Class(String name) {
		this.name = name;
		this.questions = new ArrayList<Question>();
	}
	public Class() {
		this.questions = new ArrayList<Question>();
	}
	
	public void addQuestion(Question question) {
		questions.add(question);
	}
	public void removeQuestion(Question question) {
		questions.remove(question);
	}

	
	public int getSizeQuestions() {
		return questions.size();
	}
	
	public String getName() {
		return name;
	}
	
	
	public ArrayList<Question> getQuestions(){
		return questions;
	}
}
