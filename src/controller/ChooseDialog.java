package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Question;
import view.ChooseClassDialogController;
/**
 * Open a dialog to choose in which class to put the question
 * @author Dell'omo
 */
public class ChooseDialog implements Runnable{
	private MainApp mainApp;
	private CountDownLatch countDownLatch;
	private AnchorPane page;
	private Stage dialogStage;
	
	
	private ArrayList<Question> questions;
	   
	/**
	 * Constructor of the Runnable
	 * @param mainApp The controller mainApp
	 * @param questions All the questions to classified
	 */
	public ChooseDialog(MainApp mainApp,ArrayList<Question> questions)
	{
		this.mainApp = mainApp;
		this.questions = questions;
	}
	
	/**
	 * Allow to use a countDownLatch to synchronize the thread
	 * @param countDownLatch the countDownLatch must be initialized to one
	 * @return the object that called the function
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
            controller.initDialog(mainApp,questions);
            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.setOnCloseRequest(e -> e.consume());
            dialogStage.setTitle("Question in class");
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setX(mainApp.getPrimaryStage().getX()+mainApp.getPrimaryStage().getWidth());
            dialogStage.setY(mainApp.getPrimaryStage().getY());
            Scene scene = new Scene(page);            
            dialogStage.setScene(scene);
	        dialogStage.showAndWait();
			if(countDownLatch != null)//If the main stage must wait to continue
            	countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

}
