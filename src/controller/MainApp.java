package controller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import factory.QuestionFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Class;
import model.Question;
import view.ListOverviewController;
/**
 * Main Controller of the application
 * @author Florent
 *
 */
public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private Scene scene;
    private ListOverviewController listOverviewController;
    private ObservableList<Class> classData = FXCollections.observableArrayList();
    private DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private Instance instance;
    private int classifiedQuestionsCount = 0;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Provençal le Gaulois");
        initRootLayout();
        String filepath = showStartAlert();
        Task<Void> classifyTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                classifyQuestions(filepath);
                return null;
            }
        };
        Thread classifyThread = new Thread(classifyTask);
        classifyThread.setDaemon(true);
        classifyThread.start();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ListOverview.fxml"));
            rootLayout = (BorderPane) loader.load();
            scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
            	e.consume();
            	showExitAlert(e);
            });
            listOverviewController = loader.getController();
            listOverviewController.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
     * Classify the questions and update the progress or open a dialog if needed
     */
    public void classifyQuestions(String filepath) {
    	if(filepath.isEmpty()) {
    		createNewInstance();
    	}else {
    		loadPreviousInstance(filepath);
    		classData.setAll(instance.keyMapping.values());
    	}
    	for(Question question : instance.getQuestions().values()) {
    		if(question.isClassified()) {
    			classifiedQuestionsCount++;
    		}
    	}
    	int total = instance.getQuestionsCount();
   		progress.set((double)classifiedQuestionsCount/total);//How to set the progress
   		ArrayList<Question> questions = new ArrayList<Question>(instance.getQuestions().values());
   		Platform.runLater(new ChooseDialog(this,questions));
    }
    private void loadPreviousInstance(String filepath) {
    	CountDownLatch countDownLatch = new CountDownLatch(1);
    	Platform.runLater(
    			new LoadingScreen(this, countDownLatch)
		);
    	instance = instance.loadInstance(filepath);
    	countDownLatch.countDown();
	}

	private void createNewInstance() {
      	QuestionFactory questionFactory = new QuestionFactory();
    	CountDownLatch countDownLatch = new CountDownLatch(1);
    	Platform.runLater(
    			new LoadingScreen(this, countDownLatch)
		);
    	String filepathQuestions = "resources/questions.ser";
    	instance.setQuestions(questionFactory.getAllSerializedQuestions(filepathQuestions));
    	countDownLatch.countDown();
    	countDownLatch  = new CountDownLatch(1);
    	Platform.runLater(
    			new KeyBindingDialog(this).withCountdown(countDownLatch)
    	);
    	try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Update the progressBar and the grid of classes
	 */
	public void updateProgress(int raise) {
		classifiedQuestionsCount+=raise;
		this.progress.set((double)classifiedQuestionsCount/instance.getQuestionsCount());
		classData.setAll(instance.keyMapping.values());
	}
	/**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Returns the classData
     * @return
     */
    public ObservableList<Class> getClassData(){
    	return classData;
    }
    /**
     * Return the progress of the classification
     * @return
     */
    public DoubleProperty getProgressProperty() {
    	return progress;
    }
    
    public HashMap<KeyCode,Class> getKeyMapping(){
    	return instance.keyMapping;
    }
    
    public void setKeyMapping(HashMap<KeyCode,Class> keyMapping) {
    	 instance.keyMapping = keyMapping;
    }
    
    /**
     * Open a file chooser to decide where to save the instance
     * @return boolean, true if the file is saved false otherwise
     */
    public boolean saveInstance() {
    	FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(getPrimaryStage());
		if(file != null) {
	    	instance.saveInstance(file.getAbsolutePath());
			return true;
		}
		return false;
    }    
    /**
     * Close the application
     */
    public void close() {
    	System.exit(0);
    }
    public String showStartAlert() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	((Stage)alert.getDialogPane().getScene().getWindow())
    		.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
        alert.setTitle("Startup option");
    	alert.setHeaderText("Choose your startup option");
    	alert.setContentText("The load button allows you to load a previous save file.");

    	ButtonType newButton = new ButtonType("New");
    	ButtonType loadButton = new ButtonType("Load");
    	alert.getButtonTypes().setAll(newButton, loadButton);

    	Optional<ButtonType> result = alert.showAndWait();
    	instance = new Instance();
    	if (result.get() == loadButton){
    		FileChooser fileChooser = new FileChooser();
    		File file = fileChooser.showOpenDialog(primaryStage);
    		if(file != null)
    			return file.getAbsolutePath();
    	}
    	return "";
    }
    
    private void showExitAlert(WindowEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	((Stage)alert.getDialogPane().getScene().getWindow())
		.getIcons().add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
    	ButtonType saveButton = new ButtonType("Save & Quit");
    	ButtonType exitButton = new ButtonType("Quit");
    	ButtonType cancelButton = new ButtonType("Cancel");
    	alert.getButtonTypes().setAll(saveButton, exitButton,cancelButton);

        alert.setTitle("Confirm exit");
        alert.setHeaderText("Do you want to save before exit ?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==saveButton) {
        	if(saveInstance())
        		close();
        }else if(result.get()==exitButton) {
        	close();
        }
	}
    /**
     * Launch the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}