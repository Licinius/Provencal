package stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import controller.MainApp;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingScreenStage extends Stage{

	private CountDownLatch countDownLatch;
	public LoadingScreenStage(MainApp mainApp,CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/view/LoadingView.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        // Create the dialog Stage.
	        this.getIcons().add(new Image(getClass().getResourceAsStream("/view/resources/images/icon.png")));
	        this.setResizable(false);
	        this.initModality(Modality.APPLICATION_MODAL);
	        this.initStyle(StageStyle.TRANSPARENT);
	        this.initOwner(mainApp.getPrimaryStage());
	        Scene scene = new Scene(page);
	        this.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void showAndWait() {
	        super.show();
	        Task<Void> wait = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception {
	            	 countDownLatch.await();
	                 return null;
	            }
	        };
	        wait.setOnSucceeded( arg0 -> {
				this.close();
			});
	        Thread waitingThread = new Thread(wait);
	        waitingThread.setDaemon(true);
	        waitingThread.start();
	}

}
