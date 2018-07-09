package controller;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingScreen implements Runnable{

	private MainApp mainApp;
	private CountDownLatch countDownLatch;
	public LoadingScreen(MainApp mainApp,CountDownLatch countDownLatch) {
		this.mainApp = mainApp;
		this.countDownLatch = countDownLatch;
	}
	@Override
	public void run() {
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(ChooseDialog.class.getResource("/view/LoadingView.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
	        dialogStage.setResizable(false);
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initStyle(StageStyle.TRANSPARENT);
	        dialogStage.initOwner(mainApp.getPrimaryStage());
	        Scene scene = new Scene(page);
	        
	        dialogStage.setScene(scene);
	        dialogStage.show();
	        Task<Void> wait = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception {
	            	 countDownLatch.await();
	                 return null;
	            }
	        };
	        wait.setOnSucceeded( arg0 -> {
				dialogStage.close();
			});
	        Thread waitingThread = new Thread(wait);
	        waitingThread.setDaemon(true);
	        waitingThread.start();
	       
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}

}
