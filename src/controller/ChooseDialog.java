package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Question;
import view.ChooseClassDialogController;
/**
 * Open a dialog to choose in wich class to put the question
 * @author Dell'omo
 */
public class ChooseDialog implements Runnable{
	private MainApp mainApp;
	private CountDownLatch countDownLatch;
	private AnchorPane page;
	private Stage dialogStage;
	private Question question;
	
	/**
	 * Constructor of the Runnable
	 * @param mainApp The controller mainApp
	 * @param question The question to put in the class
	 */
	public ChooseDialog(MainApp mainApp,Question question)
	{
		this.mainApp = mainApp;
		this.question = question;
	}
	
	/**
	 * Allow to use a countDownLatch to synchronize the thread
	 * @param countDownLatch
	 * @return this
	 */
	public ChooseDialog withCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
		return this;
	}
	
	/**
	 * This function is the "main" of the runnable
	 * It loads the dialog and remove question from the class if there are selected
	 */
	@Override
	public void run() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChooseDialog.class.getResource("/view/ChooseClassDialog.fxml"));
            page = (AnchorPane) loader.load();
            ChooseClassDialogController controller = loader.getController();
            controller.initDialog(mainApp,question);
            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle("Question in class");
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
	        dialogStage.showAndWait();
	        if(countDownLatch != null)//If the main stage must wait to continue
            	countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * This function create an alert and return the result
	 * @param questions, an arrayList of Question
	 * @return True if the user click OK, false otherwise
	 */
	private boolean createAlert(ArrayList<Question> questions) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove question");
        alert.setHeaderText("Do you really want to remove this questions of the class ?");
        String questionsId = ""; 
        for(Question q: questions) {
        	questionsId += "\n - " +q.getId();
        }
        alert.setContentText("The next questions will be removed"+questionsId);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
	}

}
