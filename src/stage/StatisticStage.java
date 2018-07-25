package stage;

import java.io.IOException;

import controller.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.StatisticViewController;

public class StatisticStage extends Stage{
	public StatisticStage (MainApp mainApp) {
		super();
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/view/StatisticView.fxml"));
	        BorderPane page = (BorderPane) loader.load();
	        StatisticViewController controller = loader.getController();
		    controller.setClasses(mainApp);
	        // Create the dialog Stage.
	        this.getIcons().add(new Image(getClass().getResourceAsStream("/view/resources/images/icon.png")));
	        this.initOwner(mainApp.getPrimaryStage());
	        Scene scene = new Scene(page);
	        this.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
