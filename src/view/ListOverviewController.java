package view;

import controller.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Class;
import stage.KeyBindingStage;
/**
 * Class manage the ListOverView
 * This class is connected with the MainApp
 * @author Dell'omo
 *
 */
public class ListOverviewController {
	@FXML
	private ProgressIndicator percentage;
	@FXML
	private ProgressBar progressBar;
	@FXML 
	private TableView<Class> classTable;
	@FXML
    private TableColumn<Class, String> nameClassColumn;
    @FXML
    private TableColumn<Class, Number> numberQuestionColumn;

	private MainApp mainApp;
	@FXML
	private void initialize() {
		nameClassColumn.setCellValueFactory(
                new PropertyValueFactory<Class, String>("name"));
		numberQuestionColumn.setCellValueFactory(
                new PropertyValueFactory<Class, Number>("questionCount"));
	}
	/**
	 * This function will bind the property of progress to the progress bar in the view
	 * @param mainApp The MainApp of the application
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		classTable.setItems(mainApp.getClassData());
		progressBar.progressProperty().bind(mainApp.getProgressProperty());
		percentage.progressProperty().bind(mainApp.getProgressProperty());
	}
	/**
	 * Allow to save the state of the Application
	 * @return boolean true if the application is saved
	 */
	@FXML
	private boolean saveApplication() {
		return mainApp.saveInstance();
	}
	
	/**
	 * Allow to save and close the application
	 */
	@FXML
	private void saveAndCloseApplication() {
		if(saveApplication())
			closeApplication();
	}
	
	/**
	 * Allow to close the application, the same way when the user clicks on the exit button
	 */
	@FXML
	private void closeApplication() {
		mainApp.close();
	}
	
	/**
	 * Show an alert to display the mapping of classes
	*/
	@FXML
	private void displayMapping() {
		mainApp.displayMapping();
	}
	
	/**
	 * Show the statistic view
	 */
	@FXML
	private void displayStatistic() {
		mainApp.displayStatistic();
	}
	/**
	 * Allow to create a new class
	 */
	@FXML
	private void addClass() {
		new KeyBindingStage(mainApp).showAndWait();
	}
}
