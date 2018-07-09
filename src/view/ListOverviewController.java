package view;

import controller.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Class;
/**
 * Class manage the ListOverView
 * This class is connected with the MainApp
 * @author Florent
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

	
	@FXML
	private void initialize() {
		nameClassColumn.setCellValueFactory(
                new PropertyValueFactory<Class, String>("name"));
		numberQuestionColumn.setCellValueFactory(
                new PropertyValueFactory<Class, Number>("sizeQuestions"));
	}
	
	public void setMainApp(MainApp mainApp) {
		classTable.setItems(mainApp.getClassData());
		progressBar.progressProperty().bind(mainApp.getProgressProperty());
		percentage.progressProperty().bind(mainApp.getProgressProperty());
	}
}
