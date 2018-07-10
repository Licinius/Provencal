package view;

import controller.MainApp;
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
	
	private MainApp mainApp;
	private Question question;
	
	/**
	 * Set the information of a question in the view
	 * @param mainApp
	 * @param question
	 */
	public void setQuestion(MainApp mainApp, Question question){
		this.mainApp = mainApp;
		this.question = question;
		this.title.setText(question.getTitle());
		this.body.setText(question.getBody());
	}
	
	/**
	 * Called when the user clicks on the see more button.
	 */
	@FXML
	private void handleSeeMore() {
		String url = "https://stackoverflow.com/questions/"+question.getId();
	    mainApp.getHostServices().showDocument(url);
	}
	
	public Question getQuestion() {
		return this.question;
	}
}
