package view;

import java.io.IOException;

import controller.MainApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
	
	private MainApp mainApp;
	private Question question;
	/**
	 * Init the panesControllers to an empty HashMap and
	 * The "removed" attributed to false
	 */
	public void initialize() {
		gridPane.setOnKeyPressed(new KeyPressed());
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
		this.question = question;
        AnchorPane questionPane = getQuestionPane(question);
        gridPane.add(questionPane, 0, 0);
	}
	/**
	 * Event handler to read each keyPressed
	 * @author Lenovo
	 *
	 */
	private class KeyPressed implements EventHandler<KeyEvent>{

		@Override
		public void handle(KeyEvent arg0) {
			if(mainApp.getKeyMapping().containsKey(arg0.getCode())) {
				mainApp.getKeyMapping().get(arg0.getCode()).addQuestion(question);
				((Stage)gridPane.getScene().getWindow()).close();
			}
		}
		
	}
}
