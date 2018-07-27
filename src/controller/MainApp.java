package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import achievement.EnumAchievements;
import factory.QuestionFactory;
import javafx.application.Application;
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
import model.Classes;
import model.Question;
import stage.ChooseClassStage;
import stage.KeyBindingStage;
import stage.LoadingScreenStage;
import stage.MappingStage;
import stage.StatisticStage;
import view.AlertException;
import view.ListOverviewController;

/**
 * Main Controller of the application
 * 
 * @author Dell'omo
 *
 */
public class MainApp extends Application {
	private Stage primaryStage;
	private ListOverviewController listOverviewController;
	private ObservableList<Class> classData = FXCollections.observableArrayList();
	private ObservableList<Map.Entry<KeyCode,Class>> mappingData = FXCollections.observableArrayList();; 
	private DoubleProperty progress = new SimpleDoubleProperty(0.0);
	private Instance instance;
	private int classifiedQuestionsCount = 0;
	
	/**
	 * Start the application, i.e. initialize the rootLayout, launch the task to classify
	 * the question
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Provençal le Gaulois");
		initRootLayout();
		String filepath = showStartAlert();
		if (filepath.isEmpty()) {
			createNewInstance();
		}
		else {
			loadPreviousInstance(filepath);		
		}
	}
	
	/**
	 * @return the primary stage of the application
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * 
	 * @return Returns the classData
	 */
	public ObservableList<Class> getClassData() {
		return classData;
	}
	
	/**
	 * 
	 * @return Returns the Mapping in a observable list
	 */
	public ObservableList<Map.Entry<KeyCode,Class>> getMappingData(){
		return mappingData;
	}
	
	/**
	 * @return the progress property of the classification
	 */
	public DoubleProperty getProgressProperty() {
		return progress;
	}
	
	/**
	 * 
	 * @return The classes of the applications
	 */
	public Classes getClasses() {
		return new Classes(instance.getKeyMapping().values());
	}

	/**
	 * 
	 * @return the instance of the application
	 */
	public Instance getInstance() {
		return instance;
	}
	/**
	 * @param keyMapping the keyMapping to set the instance key mapping 
	 */
	public void setKeyMapping(HashMap<KeyCode, Class> keyMapping) {
		instance.setKeyMapping(keyMapping);
		mappingData.setAll(instance.getKeyMapping().entrySet());
	}
	
	/**
	 * Initializes the root layout.
	 * <ul>
	 * <li> The stage with the view
	 * <li> The icon of the application
	 * <li> Set the close event to display an alert
	 * </ul>
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/ListOverview.fxml"));
			BorderPane rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.getIcons()
					.add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> {
				showExitAlert(e);
			});
			listOverviewController = loader.getController();
			listOverviewController.setMainApp(this);
		} catch (IOException e) {
			new AlertException(e).showAlert();
		}
	}

	/**
	 * This function will load the previous instance meanwhile displaying a loading screen
	 * 
	 * @param filepath the absolute path of the instance
	 */
	private void loadPreviousInstance(String filepath) {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		Task<Void> loadInstance = loadInstance(filepath);
		loadInstance.setOnSucceeded((arg0) -> {
			countDownLatch.countDown();
			showChooseClass();
		});
		new Thread(loadInstance).start();
		new LoadingScreenStage(this, countDownLatch).showAndWait();
		
	}

	/**
	 * Create a new instance, firstly this function will load the questions and then
	 * ask the user to map his keys
	 */
	private void createNewInstance() {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		Task<Void> loadQuestions = loadQuestions();
		loadQuestions.setOnSucceeded((arg0) -> {
			countDownLatch.countDown();
			showKeyBindingDialog();
		});
		new Thread(loadQuestions).start();
		new LoadingScreenStage(this, countDownLatch).showAndWait();		

	}
	
	/**
	 * Function to map the key after creating a new Instance
	 */
	private void showKeyBindingDialog() {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		new KeyBindingStage(this).withCountdown(countDownLatch).showAndWait();
		try {
			countDownLatch.await();
			showChooseClass();
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.close();
		}
	}
	
	/**
	 * Function fired to show the first ChooseClass
	 */
	private void showChooseClass() {
		int total = instance.getQuestionCount();
		progress.set((double) classifiedQuestionsCount / total);// How to set the progress
		new ChooseClassStage(this).showAndWait();
	}
	
	/**
	 * Update the progress with the increment, the progressBar and classData will be
	 * update
	 * 
	 * @param increment
	 *            the increment of question classified (can be negative)
	 */
	public void updateProgress(int increment) {
		classifiedQuestionsCount += increment;
		double newProgress = (double) classifiedQuestionsCount / instance.getQuestionCount();
		if(newProgress>0.5)
			instance.getAchievementManager().displayAchievement(EnumAchievements.ACH_MID_CLASS);
		this.progress.set(newProgress);
		classData.setAll(instance.getKeyMapping().values());
	}



	/**
	 * Open a file chooser to decide where to save the instance
	 * @return boolean, true if the file is saved false otherwise
	 */
	public boolean saveInstance() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(getPrimaryStage());
		if (file != null) {
			try {
				instance.saveInstance(file.getAbsolutePath());
			} catch (IOException e) {
				new AlertException(e).showAndWait();
			}
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
	
	/**
	 * This function display a window with the classes mapping
	 */
	public void displayMapping() {
		MappingStage.showMapping(this);
	}
	
	/**
	 * This function display a window with the statistic 
	 */
	public void displayStatistic() {
		new StatisticStage(this).showAndWait();
	}
	
	/**
	 * This function is fired to display an alert with 2 choices
	 * <ul>
	 * <li>Load : This choice will enable the user the load a previous instance
	 * <li>New : This choice will create a new instance and allow the user to bind
	 * key to class
	 * </ul>
	 * 
	 * @return An empty string if the user clicks on new or cancel the FileChooser,
	 *         or else the absolute path of the instance serialized
	 */
	public String showStartAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
				.add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
		alert.setTitle("Startup option");
		alert.setHeaderText("Choose your startup option");
		alert.setContentText("The load button allows you to load a previous save file.");

		ButtonType newButton = new ButtonType("New");
		ButtonType loadButton = new ButtonType("Load");
		alert.getButtonTypes().setAll(newButton, loadButton);

		Optional<ButtonType> result = alert.showAndWait();
		instance = new Instance();
		if (result.get() == loadButton) {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null)
				return file.getAbsolutePath();
		}
		return "";
	}
	
	/**
	 * This function shows an alert when call<br>
	 * This alert is used to know if the user wants to quit, save &amp; quit or cancel
	 * the operation
	 * 
	 * @param event
	 *   A WindowEvent to consume
	 */
	public void showExitAlert(WindowEvent event) {
		event.consume();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
				.add(new Image(MainApp.class.getResourceAsStream("/view/resources/images/icon.png")));
		ButtonType saveButton = new ButtonType("Save & Quit");
		ButtonType exitButton = new ButtonType("Quit");
		ButtonType cancelButton = new ButtonType("Cancel");
		alert.getButtonTypes().setAll(saveButton, exitButton, cancelButton);

		alert.setTitle("Confirm exit");
		alert.setHeaderText("Do you want to save before exit ?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == saveButton) {
			if (saveInstance())
				close();
		} else if (result.get() == exitButton) {
			close();
		}else {
			instance.getAchievementManager().displayAchievement(EnumAchievements.ACH_ONE_MORE);
		}
	}
	
	/**
	 * @return a Task to load the question from the questions.ser in the resources
	 */
	private Task<Void> loadQuestions(){
		return new Task<Void>() {
	        @Override
	        protected Void call() {
	        	QuestionFactory questionFactory = new QuestionFactory();
				String filepathQuestions = "resources/questions.ser";
				instance.setQuestions(questionFactory.getAllSerializedQuestions(filepathQuestions));	
				String filepathKeywords = "resources/keywords.ser";
				instance.setKeywords(instance.generateKeywords(filepathKeywords));
				return null;
	        }
	    };
	}
	
	/**
	 * 
	 * @param filepath The absolute path to the instance
	 * @return a Task to load the instance 
	 */
	private Task<Void> loadInstance(String filepath){
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				instance = instance.loadInstance(filepath);
				classData.setAll(instance.getKeyMapping().values());
				mappingData.setAll(instance.getKeyMapping().entrySet());
				for (Question question : instance.getQuestions().values()) {
					if (question.isClassified()) {
						classifiedQuestionsCount++;
					}
				}
				return null;
			}
		};
	}
	
	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main(). 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}