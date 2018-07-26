package stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import controller.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.AlertException;
import view.KeyBindingController;
public class KeyBindingStage extends Stage{
	private CountDownLatch countDown;
	private MainApp mainApp;
	private KeyBindingController controller;
	/**
	 * Constructor of the Runnable
	 * @param mainApp The controller mainApp 
	 */
	public KeyBindingStage(MainApp mainApp){
		this.mainApp = mainApp;
		// Load the fxml file and create a new stage for the popup dialog.
		 try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/view/KeyBindingView.fxml"));
			AnchorPane page = loader.load();
			controller = loader.getController();
	        controller.setMainApp(this.mainApp);
	        // Create the dialog Stage.
	        this.setResizable(false);
	        this.setTitle("Bind keys for classes");
	        this.initOwner(mainApp.getPrimaryStage());
			this.setOnCloseRequest(e -> e.consume());
	        this.initModality(Modality.WINDOW_MODAL);
	        this.getIcons().add(new Image(getClass().getResourceAsStream("/view/resources/images/icon.png")));
	        Scene scene = new Scene(page);
	        this.setScene(scene);
	        this.sizeToScene();
		 } catch (IOException e1) {
			new AlertException(e1).showAlert();
		}
	}
	/**
	 *A countdown can be used to make the comportment synchronous
	 * @param countdown the countdownLatch initialize
	 * @return the current object
	 */
	public KeyBindingStage withCountdown(CountDownLatch countdown) {
		this.countDown = countdown;
		return this;
	}


	/**
	 * Show the keybinding dialog and wait, if the keybinding is not validated then the dialog is shown again
	 */
	@Override
	public void showAndWait() {
		do {
			super.showAndWait();
		}while(!controller.isValidated());
		mainApp.updateProgress(0);
		if(countDown != null) //If a countdown has been initialized
			countDown.countDown();
	}
}
