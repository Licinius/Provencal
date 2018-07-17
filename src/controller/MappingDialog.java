package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.AlertException;
import view.MappingViewController;

public class MappingDialog {
	MainApp mainApp;
	static Stage dialogStage = new Stage();
	public MappingDialog(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	public void show() {
		if(!dialogStage.isShowing()) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/MappingView.fxml"));
				BorderPane page = loader.load();
				((MappingViewController) loader.getController()).setMappingData(mainApp);
				dialogStage = new Stage();
				dialogStage.setTitle("Mapping");
	            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
	            dialogStage.initOwner(mainApp.getPrimaryStage());
	            Scene scene = new Scene(page);            
	            dialogStage.setScene(scene);
		        dialogStage.show();
	            dialogStage.setX(mainApp.getPrimaryStage().getX()-dialogStage.getWidth());
	            dialogStage.setY(mainApp.getPrimaryStage().getY());
			} catch (IOException e) {
				new AlertException(e).showAlert();
			}
		}else {
			dialogStage.requestFocus();
			dialogStage.setX(mainApp.getPrimaryStage().getX()-dialogStage.getWidth());
            dialogStage.setY(mainApp.getPrimaryStage().getY());
		}
	}
}
