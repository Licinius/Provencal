package stage;

import java.io.IOException;

import controller.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.AlertException;
import view.MappingViewController;

public class MappingStage extends Stage{
	MainApp mainApp;
	static Stage INSTANCE = null; 
	private MappingStage(MainApp mainApp) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/MappingView.fxml"));
			BorderPane page = loader.load();
			((MappingViewController) loader.getController()).setMappingData(mainApp);
			this.setTitle("Mapping");
			this.getIcons().add(new Image(getClass().getResourceAsStream("/view/resources/images/icon.png")));
			if(this.getOwner() != null)
				this.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);            
            this.setScene(scene);
            this.show();
            this.setX(mainApp.getPrimaryStage().getX()-this.getWidth());
            this.setY(mainApp.getPrimaryStage().getY());
		} catch (IOException e) {
			new AlertException(e).showAlert();
		}
	}
	public static void showMapping(MainApp mainApp) {
		if(INSTANCE == null){
			INSTANCE = new MappingStage(mainApp);
		}
		if(!INSTANCE.isShowing()) {
			INSTANCE.show();
		}else {
			INSTANCE.requestFocus();
			INSTANCE.setX(mainApp.getPrimaryStage().getX()-INSTANCE.getWidth());
			INSTANCE.setY(mainApp.getPrimaryStage().getY());
		}
	}
}
