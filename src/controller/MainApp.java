package controller;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
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
        instance = new Instance();
        initRootLayout();
        Task<Void> classifyTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                classifyQuestions();
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
            listOverviewController = loader.getController();
            listOverviewController.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Classify the questions and update the progress or open a dialog if needed
     */
    public void classifyQuestions() {
    	QuestionFactory questionFactory = new QuestionFactory();
    	CountDownLatch countDownLatch = new CountDownLatch(1);
    	Platform.runLater(
    			new LoadingScreen(this, countDownLatch)
		);
    	String filepath = "src/resources/questions/questions.ser";
    	instance.remainingQuestions = questionFactory.getAllSerializedQuestions(filepath);
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
    	int iteration = 0;
    	int total = instance.remainingQuestions.size();
    	for(Question question : instance.remainingQuestions.values()) {
    		countDownLatch = new CountDownLatch(1);
        	Platform.runLater(
        			new ChooseDialog(this,question).withCountDownLatch(countDownLatch)
        	);
        	try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	instance.remainingQuestions.remove(question.getId());
        	classData.setAll(instance.keyMapping.values());
        	iteration++;
        	progress.set((double)iteration/total);//How to set the progress
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
    /**
     * Launch the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private class Instance implements Serializable{
		public HashMap<KeyCode,Class> keyMapping;
    	public HashMap<Integer,Question> remainingQuestions;
    	
    	public Instance() {
    		keyMapping = new HashMap<KeyCode,Class>();
    		remainingQuestions = new HashMap<Integer,Question>();
    	}
    	
		public void saveInstance(String filepath) {
    		FileOutputStream fos;
    		try {
    			fos = new FileOutputStream(filepath);
    			ObjectOutputStream oos = new ObjectOutputStream(fos);
    			oos.writeObject(this);
    			oos.close();
    		} catch (IOException  e) {
    			e.printStackTrace();
    		}
    	}
    }
}