package controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
    public MainApp() {
    
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Kadoc");
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
    	//keywordsClasses'll simulate the applications of classes
    	ArrayList<Class> classes = new ArrayList<Class>();
    	ArrayList<String> keywords1 = new ArrayList<String>();
    	keywords1.add("truc");keywords1.add("toto");
    	Class class1 = new Class("A",keywords1);
    	ArrayList<String> keywords2 = new ArrayList<String>();
    	keywords2.add("pouet");keywords2.add("toto");
    	Class class2 = new Class("B",keywords2);
    	
    	classes.add(class1);classes.add(class2);
		ArrayList<String> questions = new ArrayList<>();
		questions.add("toto");
	    questions.add("machin");questions.add("machin");
	    questions.add("pouet");
	    questions.add("toto");
	    questions.add("toto");questions.add("machin");
	    questions.add("something");
		int iteration = 0; //iteration can be the id currently test
    	int total = questions.size(); //Total can be the max id 
    	//Loop'll simulate the application looping on the questions
    	for(String s : questions) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Class winner = null;
    		for(Class c : classes) {
    			if (c.contains(s)) {
    				if(winner == null)
    					winner = c;
    				else {//How to display the choose Dialog
    					if(winner.getQuestions().size()>0) {
	    					CountDownLatch finishCountDown = new CountDownLatch(1);//Semaphore
	    					Platform.runLater(
	    						new ChooseDialog(this,winner)
	    							.withCountDownLatch(finishCountDown)
	    					);
	    					try {
								finishCountDown.await();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
    					}
    	            }
    			}
    		}
    		if(winner != null) {
    			winner.addQuestion(new Question());
	    		if(!classData.contains(winner)) 
	    			classData.add(winner);
	    		else {
	    		    int index = classData.indexOf(winner);
	    		    if (index >= 0) {
	    		    	classData.set(index, winner);
	    		    }
	    		}
    		}
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
    /**
     * Launch the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}