package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Class;
import view.KeyBindingController;
public class KeyBindingDialog implements Runnable{
	private Stage dialogStage;
	private AnchorPane page;
	private MainApp mainApp;
	private HashMap<KeyCode,Class> keyMapping;
	private CountDownLatch countDown;
	/**
	 * Constructor of the Runnable
	 * @param mainApp The controller mainApp 
	 */
	public KeyBindingDialog(MainApp mainApp){
		this.mainApp = mainApp;
		this.keyMapping = keyMapping;
	}
	
	public KeyBindingDialog withCountdown(CountDownLatch countdown) {
		this.countDown = countdown;
		return this;
	}
	@Override
	public void run() {
		 try {
	            // Load the fxml file and create a new stage for the popup dialog.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(ChooseDialog.class.getResource("/view/keyBindingView.fxml"));
	            page = loader.load();
	            KeyBindingController controller = loader.getController();
	            controller.setMainApp(this.mainApp);
	            // Create the dialog Stage.
	            dialogStage = new Stage();
	            dialogStage.setResizable(false);
	            dialogStage.setOnCloseRequest(e -> e.consume());
	            dialogStage.setTitle("Bind keys for classes");
	            dialogStage.initOwner(mainApp.getPrimaryStage());
	            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            Scene scene = new Scene(page);
	            dialogStage.setScene(scene);
	            dialogStage.sizeToScene();
	            while(!controller.isValidated()) {
		            dialogStage.showAndWait();
	            }
	            if(countDown != null) {
	            	countDown.countDown();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}

}
