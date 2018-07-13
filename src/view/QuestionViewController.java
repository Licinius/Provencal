package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import model.Class;
import model.Question;
/**
 * Controller for an anchor pane that display a question
 * @author Dell'omo
 */
public class QuestionViewController{
	@FXML
	private Label title;
	@FXML
	private WebView body;
	@FXML
	private Label classes;
	@FXML
	private GridPane gridPane;
	private Question question;
	
	/**
	 * Set the information of a question in the view
	 * @param mainApp
	 * @param question
	 */
	public void setQuestion(Question question){
		this.question = question;
		this.title.setText(question.getTitle());
		this.body.getEngine().loadContent(question.getBody());
		if(question.isClassified()) {
			this.title.setStyle(this.title.getStyle()+"-fx-text-fill: #64DD17;");
		}
		String classesName = "";
		for(Class aClass : question.getClasses()) {
			classesName+=aClass.getName() + ", ";
		}
		this.classes.setText(classesName);
	}
	
	
	public Question getQuestion() {
		return this.question;
	}
}
