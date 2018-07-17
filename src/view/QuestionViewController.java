package view;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import model.Question;
/**
 * Controller for an anchor pane that display a question
 * @author Dell'omo
 */
public class QuestionViewController {
	@FXML
	private Label title;
	@FXML
	private WebView body;
	@FXML
	private Label classes;
	private Question question;
	
	/**
	 * Set the information of a question in the view
	 * @param question The question to set
	 */
	public void setQuestion(Question question){
		this.question = question;
		this.title.setText(question.getTitle());
		this.body.getEngine().loadContent(question.getBody());
		if(question.isClassified()) {
			this.title.setStyle(this.title.getStyle()+"-fx-text-fill: #64DD17;");
		}
		String classesName = question.getClasses().stream().map(p -> p.getName())
                .collect(Collectors.joining(", "));
		this.classes.setText(classesName);
	}
	
	/**
	 * 
	 * @return the question in the View
	 */
	public Question getQuestion() {
		return this.question;
	}
}
