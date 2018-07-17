package controller;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.AlertException;
import view.KeyBindingController;
public class KeyBindingDialog implements Runnable{
	private Stage dialogStage;
	private AnchorPane page;
	private MainApp mainApp;
	private CountDownLatch countDown;
	private KeyBindingController controller;
	/**
	 * Constructor of the Runnable
	 * @param mainApp The controller mainApp 
	 */
	public KeyBindingDialog(MainApp mainApp){
		this.mainApp = mainApp;
	}
	
	public KeyBindingDialog withCountdown(CountDownLatch countdown) {
		this.countDown = countdown;
		return this;
	}
	@Override
	public void run() {
		showKeyBindingDialog();
        if(countDown != null) 
        	countDown.countDown();    
	}

	public void showKeyBindingDialog() {
		// Load the fxml file and create a new stage for the popup dialog.
		 try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(ChooseDialog.class.getResource("/view/keyBindingView.fxml"));
			page = loader.load();
	        controller = loader.getController();
	        controller.setMainApp(this.mainApp);
	        // Create the dialog Stage.
	        dialogStage = new Stage();
	        dialogStage.setResizable(false);
	        dialogStage.setTitle("Bind keys for classes");
	        dialogStage.initOwner(mainApp.getPrimaryStage());
			dialogStage.setOnCloseRequest(e -> e.consume());
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        dialogStage.sizeToScene();
	        do {
	        	dialogStage.showAndWait();
	        }while(!controller.isValidated());
	        mainApp.updateProgress(0);
		 } catch (IOException e1) {
			new AlertException(e1).showAlert();
		}
	}

}
