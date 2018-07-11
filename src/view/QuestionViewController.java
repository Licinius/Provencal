package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.Question;

/**
 * Controller for an anchor pane that display a question
 * @author Dell'omo
 */
public class QuestionViewController{
	@FXML
	private Label title;
	@FXML
	private Label body;
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
		this.body.setText(question.getBody());
	}
	
	
	public Question getQuestion() {
		return this.question;
	}
}
