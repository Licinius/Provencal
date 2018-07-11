package controller;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
            primaryStage.setOnHidden(e -> System.exit(0));
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
    	CountDownLatch countDownLatch;
    	int iteration;
    	if(filepath.isEmpty()) {
    		createNewInstance();
        	iteration = 0;
    	}else {
    		loadPreviousInstance(filepath);
    		iteration = instance.getInitialQuestionsCount() - instance.remainingQuestions.size();
    		classData.setAll(instance.keyMapping.values());
    	}
    	int total = instance.getInitialQuestionsCount();
   		progress.set((double)iteration/total);//How to set the progress
    	Iterator<Question> iteratorQuestions = instance.remainingQuestions.values().iterator();
    	Question question;
    	while(iteratorQuestions.hasNext()) {
    		question = iteratorQuestions.next();
    		countDownLatch = new CountDownLatch(1);
        	Platform.runLater(
        			new ChooseDialog(this,question).withCountDownLatch(countDownLatch)
        	);
        	try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	classData.setAll(instance.keyMapping.values());
        	iteration++;
        	iteratorQuestions.remove(); //Also remove in the HashMap
        	progress.set((double)iteration/total);//How to set the progress
    	}
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
    	String filepathQuestions = "src/resources/questions/questions.ser";
    	instance.setRemainingQuestions(questionFactory.getAllSerializedQuestions(filepathQuestions));
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
    
    public void saveInstance(String filepath) {
    	instance.saveInstance(filepath);
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
    		fileChooser.setTitle("Open Resource File");
    		File file = fileChooser.showOpenDialog(new Stage());
    		return file.getAbsolutePath();
    	}else {
    		return "";
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