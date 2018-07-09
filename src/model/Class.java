package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
	private StringProperty  name;
	private ObjectProperty<ArrayList<String>> keywords;
	private ObjectProperty<ArrayList<Question>> questions;
	private IntegerProperty sizeQuestions;
	
	public Class(String name,ArrayList<String> keywords) {
		this.name = new SimpleStringProperty(name);
		this.keywords = new SimpleObjectProperty<>(keywords);
		this.questions = new SimpleObjectProperty<>(new ArrayList<Question>());
		this.sizeQuestions = new SimpleIntegerProperty(0);
	}
	public Class() {}
	
	public void addQuestion(Question question) {
		questions.get().add(question);
		sizeQuestions.set(questions.get().size());
	}

	public IntegerProperty getSizeQuestionsProperty() {
		return sizeQuestions;
	}
	public int getSizeQuestions() {
		return sizeQuestions.get();
	}
	
	public boolean contains(String keyword) {
		return keywords.get().contains(keyword);
	}
	public StringProperty getNameProperty() {
		return name;
	}
	public String getName() {
		return name.get();
	}
	
	public ObjectProperty<ArrayList<String>> getKeywordsProperty(){
		return keywords;
	}
	public ArrayList<String> getKeywords(){
		return keywords.get();
	}
	
	public ObjectProperty<ArrayList<Question>> getQuestionsProperty(){
		return questions;
	}
	public ArrayList<Question> getQuestions(){
		return questions.get();
	}
}
