package view;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class KeyBindingController {
	@FXML
	private GridPane gridPane;
	@FXML
	private JFXButton addClassbutton;
	
	private ArrayList<TextField> classNameFields;
	private ArrayList<KeyBindingField> keyBindingFields;
	private int row;
	private boolean validated;
	
	@FXML
	private void initialize() {
		classNameFields = new ArrayList<TextField>();
		keyBindingFields = new ArrayList<KeyBindingField>();
		row = 1;
		validated = false;
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
		validated = true;
		((Stage) gridPane.getScene().getWindow()).close();
	}
	
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
		
		public void setCode(KeyCode code) {
			this.code = code;
		}
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
				keyBindingField.clear();
				keyBindingField.setText(arg0.getCode().toString());
				keyBindingField.setCode(arg0.getCode());
			}
		}
	}
	
}
