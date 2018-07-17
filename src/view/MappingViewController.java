package view;

import java.util.Map;

import controller.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import model.Class;

public class MappingViewController {
	@FXML
	private TableView<Map.Entry<KeyCode,Class>> table;
	@FXML
	private TableColumn<Map.Entry<KeyCode,Class>,String> nameClassColumn;
	@FXML
	private TableColumn<Map.Entry<KeyCode,Class>,String> keyCodeColumn;
	
	/**
	 * Initialize the column with a listener on the values of the TableView
	 */
	public void initialize() {
		keyCodeColumn.setCellValueFactory(
			new Callback<TableColumn.CellDataFeatures<Map.Entry<KeyCode,Class>, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Map.Entry<KeyCode,Class>, String> p) {
					return new SimpleStringProperty(p.getValue().getKey().toString());
				}
			});	
		nameClassColumn.setCellValueFactory(
			new Callback<TableColumn.CellDataFeatures<Map.Entry<KeyCode,Class>, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Map.Entry<KeyCode,Class>, String> p) {
	                return new SimpleStringProperty(p.getValue().getValue().getName());
				}
			});		
	}
	/**
	 * Set the items of the list
	 * @param keyMapping the keyMapping of the MainApp
	 */
	public void setMappingData(MainApp mainApp) {
		table.setItems(mainApp.getMappingData());
	}
}
