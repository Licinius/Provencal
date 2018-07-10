package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import controller.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Question;
/**
 * Controller for the view ChooseClassDialog
 * @author Dell'omo
 *
 */
public class ChooseClassDialogController {
	@FXML
	private GridPane gridPane;
	@FXML
	private Button doNothingButton;
	@FXML
	private Button removeButton;
	
	private MainApp mainApp;
	private HashMap<Pane,QuestionViewController> panesControllers;
	private boolean removed;
	
	/**
	 * Init the panesControllers to an empty HashMap and
	 * The "removed" attributed to false
	 */
	public void initialize() {
		panesControllers = new HashMap<Pane, QuestionViewController>();
		removed = false;
	}
	
	/**
	 * This function return an anchorPane to insert in the gridPane
	 * It also adds the controller and the pane to the Hashmap
	 * @param question a Question to format in a QuestionView
	 * @return The AnchorPane to attach to the GridPane
	 * @throws IOException
	 */
	private AnchorPane getQuestionPane(Question question) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/QuestionView.fxml"));
		AnchorPane pane = (AnchorPane)loader.load();
		QuestionViewController controller = loader.getController();
		controller.setQuestion(this.mainApp,question);
		panesControllers.put(pane,controller);
		return pane;
	}
	
	/**
	 * This function initialized the dialog with the question in the class
	 * @param mainApp
	 * @param question
	 * @throws IOException
	 */
	public void initDialog(MainApp mainApp, Question question) throws IOException {
		this.mainApp = mainApp;
        AnchorPane questionPane = getQuestionPane(question);
        gridPane.add(questionPane, 0, 0);
	}
	
	/**
	 * This function is used by the doNothing button to close the window
	 */
	@FXML
	private void doNothingButtonAction(){
		removed = false;
	    Stage stage = (Stage) doNothingButton.getScene().getWindow();
	    stage.close();
	}
	
	/**
	 * This function is used by the remove button to change the attribute removed to true
	 */
	@FXML
	private void removeButtonAction() {
		QuestionViewController questionViewController;
		for(Node n : gridPane.getChildren()) {
			questionViewController = panesControllers.get(n);
			if(questionViewController.isSelected()) {
				removed = true;
				Stage stage = (Stage) removeButton.getScene().getWindow();
			    stage.close();
			    break;
			}
		}
	}
	
	/**
	 * This function loop through the GridPane to find the questions selected
	 * @return An ArrayList of selectedQuestion
	 */
	public ArrayList<Question> getSelectedQuestion() {
		ArrayList<Question> questions = new ArrayList<Question>();
		QuestionViewController questionViewController;
		for(Node n : gridPane.getChildren()) {
			questionViewController = panesControllers.get(n);
			if(questionViewController.isSelected()) {
				questions.add(questionViewController.getQuestion());
			}
		}
		return questions;
	}
	
	/**
	 * Return the attribute removed
	 * @return True if the button "Remove" has been clicked
	 */
	public boolean isRemoved() {
		return removed;
	}

}
