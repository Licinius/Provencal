package controller;

import java.io.IOException;
import java.util.HashMap;

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
	/**
	 * Constructor of the Runnable
	 * @param mainApp The controller mainApp 
	 */
	public KeyBindingDialog(MainApp mainApp,HashMap<KeyCode,Class> keyMapping){
		this.mainApp = mainApp;
		this.keyMapping = keyMapping;
	}
	
	@Override
	public void run() {
		 try {
	            // Load the fxml file and create a new stage for the popup dialog.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(ChooseDialog.class.getResource("/view/keyBindingView.fxml"));
	            page = loader.load();
	            KeyBindingController controller = loader.getController();
	            // Create the dialog Stage.
	            dialogStage = new Stage();
	            dialogStage.setTitle("Bind keys for classes");
	            dialogStage.initOwner(mainApp.getPrimaryStage());
	            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            Scene scene = new Scene(page);
	            dialogStage.setScene(scene);
	            dialogStage.sizeToScene();
	            while(!controller.isValidated()) {
		            dialogStage.showAndWait();
		            if(controller.isValidated()) {
		            	keyMapping = controller.getKeyMapping();
		            }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}

}
