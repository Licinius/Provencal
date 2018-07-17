package view;

import java.util.ArrayList;
import java.util.HashMap;

import com.jfoenix.controls.JFXButton;

import controller.MainApp;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Class;

public class KeyBindingController {
	@FXML
	private GridPane gridPane;
	@FXML
	private JFXButton addClassbutton;
	
	private ArrayList<TextField> classNameFields;
	private ArrayList<KeyBindingField> keyBindingFields;
	private MainApp mainApp;
	private int row;
	private boolean validated;
	
	@FXML
	private void initialize() {
		classNameFields = new ArrayList<TextField>();
		keyBindingFields = new ArrayList<KeyBindingField>();
		row = 1;
		validated = false;
	}
	/**
	 * When the mainApp is set the view has to recreate the old rows and create a new one 
	 * @param mainApp the MainApp of the application
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		recreateRows();
		createRow();
	}
	/**
	 * Fired when the user click on the button addClass 
	 * This function will add a new row to add a class
	 */
	@FXML
	private void handleAddClass() {
		createRow();
		//Adapt the scene to the new row
		gridPane.getScene().getWindow().sizeToScene();
	}
	/**
	 * Handle the button validate, when clicked close the window
	 */
	@FXML
	private void handleValidate() {
		int index = 0;
		int keyCodeCount = 0;
		//The mapping will be update if not empty
		HashMap<KeyCode,Class> keyMapping = mainApp.getKeyMapping();
		for(KeyBindingField keyBindingField : keyBindingFields) {
			if(keyBindingField.getCode() != null) {
				if(!keyMapping.containsKey(keyBindingField.getCode())){
					keyMapping.put(
							keyBindingField.getCode(),
							new Class(classNameFields.get(index).getText())
							);
					keyCodeCount++;
				}
			}
			index+=1;
		}
		validated = (!validated? keyCodeCount>0 :true);
		((Stage) gridPane.getScene().getWindow()).close();
	}
	/**
	 * 
	 * @return true if the row is validated
	 */
	public boolean isValidated() {
		return validated;
	}
	
	/**
	 * Create a new row in the grid Pane à la
	 * |class_name | KeyBindingField | Add Class |
	 *  The button addClass is remove from the row before and add in the new
	 */
	private void createRow() {
		TextField classNameField = new TextField("className_" + row);
		classNameFields.add(classNameField);
		KeyBindingField keyBindingField = new KeyBindingField();
		keyBindingFields.add(keyBindingField);
		gridPane.add(classNameField, 0, row);
		gridPane.add(keyBindingField, 1,row);
		gridPane.getChildren().remove(addClassbutton);
		gridPane.add(addClassbutton, 2, row);
		row++;
	}
	/**
	 * This function recreate rows that have been already validate by the user<br>
	 * The rows are disable and not add to the arrayList of TextField and KeyBindingField
	 */
	private void recreateRows() {
		for(KeyCode keycode : mainApp.getKeyMapping().keySet()) {
			validated = true;
			Class aClass = mainApp.getKeyMapping().get(keycode);
			TextField classNameField = new TextField(aClass.getName());
			classNameField.setDisable(true);
			KeyBindingField keyBindingField = new KeyBindingField(keycode);
			keyBindingField.setDisable(true);
			gridPane.add(classNameField, 0, row);
			gridPane.add(keyBindingField, 1,row);
			row++;
		}
	}
	
	/**
	 * Specialized TextField for KeyBinding in the app
	 * @author Dell'omo
	 *
	 */
	private class KeyBindingField extends TextField{

		private KeyCode code;
		public KeyBindingField() {
			this.setOnKeyReleased(new KeyReleased(this) );
			this.setOnMouseClicked(new MouseClicked(this));
		}
		/**
		 * Create a textField with a keycode already bind to it
		 * @param code The code bind to the textField
		 */
		public KeyBindingField(KeyCode code) {
			this.code = code;
			this.setText(code.toString());
		}
		/**
		 * 
		 * @param code the KeyCode to set
		 */
		public void setCode(KeyCode code) {
			this.code = code;
		}
		/**
		 * 
		 * @return The keyCode of the textField
		 */
		public KeyCode getCode() {
			return this.code;
		}
		/**
		 * EventHandler when the user clicks on the textField
		 * @author Dell'omo
		 *
		 */
		private class MouseClicked implements EventHandler<Event>{
			private KeyBindingField keyBindingField;
			public MouseClicked(KeyBindingField keyBindingField) {
				this.keyBindingField = keyBindingField;
			}
			@Override
			public void handle(Event arg0) {
				keyBindingField.clear(); //Clear the textField
				keyBindingField.setCode(null);
			}
		}
		/**
		 * EventHandler when the user pressed a key in the textField
		 * @author Dell'omo
		 *
		 */
		private class KeyReleased implements EventHandler<KeyEvent>{
			private KeyBindingField keyBindingField;
			public KeyReleased(KeyBindingField keyBindingField) {
				this.keyBindingField = keyBindingField;
			}
			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode().isLetterKey()) {
					keyBindingField.clear();
					keyBindingField.setText(arg0.getCode().toString());
					keyBindingField.setCode(arg0.getCode());
				}
			}
		}
	}
	
}
