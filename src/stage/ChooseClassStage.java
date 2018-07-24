package stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import controller.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Question;
import view.AlertException;
import view.ChooseClassDialogController;
/**
 * Open a dialog to choose in which class to put the question
 * @author Dell'omo
 */
public class ChooseClassStage extends Stage{
	private CountDownLatch countDownLatch;	   
	/**
	 * Constructor of the Runnable
	 * @param mainApp The controller mainApp
	 * @param questions All the questions to classified
	 */
	public ChooseClassStage(MainApp mainApp,ArrayList<Question> questions){
		super();
		try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/view/ChooseClassDialog.fxml"));
	        BorderPane page = loader.load();
	        ChooseClassDialogController controller = loader.getController();
	        controller.initDialog(mainApp,questions);
	        
	        // Create the dialog Stage.
	        this.setOnCloseRequest(e ->{
	        	e.consume();
	        	mainApp.showExitAlert(e);
	        });
	        this.setTitle("Question in class");
	        this.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
	        this.initOwner(mainApp.getPrimaryStage());
	        this.setX(mainApp.getPrimaryStage().getX()+mainApp.getPrimaryStage().getWidth());
	        this.setY(mainApp.getPrimaryStage().getY());
	        Scene scene = new Scene(page);            
	        this.setScene(scene);
        } catch (IOException e) {
        	new AlertException(e).showAlert();
        }
	}
	
	/**
	 * Allow to use a countDownLatch to synchronize the thread
	 * @param countDownLatch the countDownLatch must be initialized to one
	 * @return the object that called the function
	 */
	public ChooseClassStage withCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
		return this;
	}
	
	/**
	 * This function is the "main" of the runnable
	 * It loads the dialog and remove question from the class if there are selected
	 */
	@Override
	public void showAndWait() {
        super.showAndWait();
		if(countDownLatch != null)//If the main stage must wait to continue
        	countDownLatch.countDown();
      
	}
	

}
