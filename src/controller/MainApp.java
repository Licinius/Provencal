package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	 * Start the application, i.e. init the rootLayout, launch the task to classify
	 * the question
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Provençal le Gaulois");
		initRootLayout();
		String filepath = showStartAlert();
		Task<Void> classifyTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				showFirstChooseDialog(filepath);
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
	 * This fonction will load a previous instance or create a new one if the param
	 * is empty<br>
	 * It will also count the number of question classified and init the
	 * progressBar<br>
	 * Finally, it will display the chooseDialog
	 * 
	 * @param filepath
	 *            the absolute path of the previous instance or an empty string
	 */
	public void showFirstChooseDialog(String filepath) {
		if (filepath.isEmpty()) {
			createNewInstance();
		} else {
			loadPreviousInstance(filepath);
			classData.setAll(getKeyMapping().values());
			mappingData.setAll(getKeyMapping().entrySet());
			for (Question question : instance.getQuestions().values()) {
				if (question.isClassified()) {
					classifiedQuestionsCount++;
				}
			}
		}
		int total = instance.getQuestionsCount();
		progress.set((double) classifiedQuestionsCount / total);// How to set the progress
		ArrayList<Question> questions = new ArrayList<Question>(instance.getQuestions().values());
		Platform.runLater(new ChooseDialog(this, questions));
	}

	/**
	 * This fonction will load the previous instance meanwhile displaying a loading
	 * screen
	 * 
	 * @param filepath
	 *            the absolute path of the instance
	 */
	private void loadPreviousInstance(String filepath) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Platform.runLater(new LoadingScreen(this, countDownLatch));
		try {
			instance = instance.loadInstance(filepath);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			this.close();
		}
		countDownLatch.countDown();
	}

	/**
	 * Create a new instance, firstly this function will load the questions and then
	 * ask the user to map his keys
	 */
	private void createNewInstance() {
		QuestionFactory questionFactory = new QuestionFactory();
		CountDownLatch countDownLatch = new CountDownLatch(1);
		Platform.runLater(new LoadingScreen(this, countDownLatch));
		String filepathQuestions = "resources/questions.ser";
		instance.setQuestions(questionFactory.getAllSerializedQuestions(filepathQuestions));
		countDownLatch.countDown();
		countDownLatch = new CountDownLatch(1);
		Platform.runLater(new KeyBindingDialog(this).withCountdown(countDownLatch));
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.close();
		}
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
		this.progress.set((double) classifiedQuestionsCount / instance.getQuestionsCount());
		classData.setAll(getKeyMapping().values());
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
	
	public ObservableList<Map.Entry<KeyCode,Class>> getMappingData(){
		return mappingData;
	}
	/**
	 * 
	 * @return the progress property of the classification
	 */
	public DoubleProperty getProgressProperty() {
		return progress;
	}

	/**
	 * 
	 * @return the keyMapping of the instance
	 */
	public HashMap<KeyCode, Class> getKeyMapping() {
		return instance.keyMapping;
	}

	/**
	 * 
	 * @param keyMapping
	 *            the keyMapping to sets (HashMap<KeyCode,Class)
	 */
	public void setKeyMapping(HashMap<KeyCode, Class> keyMapping) {
		instance.keyMapping = keyMapping;
		mappingData.setAll(instance.keyMapping.entrySet());
	}

	/**
	 * Open a file chooser to decide where to save the instance
	 * 
	 * @return boolean, true if the file is saved false otherwise
	 */
	public boolean saveInstance() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(getPrimaryStage());
		if (file != null) {
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
	 * This function display a window with the classes mapping
	 */
	public void displayMapping() {
		new MappingDialog(this).show();
	}

	/**
	 * This function shows an alert when call<br>
	 * This alert is used to know if the user wants to quit, save & quit or cancel
	 * the operation
	 * 
	 * @param event
	 *            A WindowEvent to consume
	 */
	private void showExitAlert(WindowEvent event) {
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
		}
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}