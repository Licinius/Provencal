package view;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Inspire by http://code.makery.ch/blog/javafx-dialogs-official/ to display an error
 * @author Dell'omo
 *
 */
public class AlertException extends Alert {

	public AlertException(Exception exception) {
		super(AlertType.ERROR);
		this.setTitle("Exception Dialog");
    	this.setHeaderText("Look, an Exception Dialog");
    	// Create expandable Exception.
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	exception.printStackTrace(pw);
    	String exceptionText = sw.toString();

    	Label label = new Label("The exception stacktrace was:");

    	TextArea textArea = new TextArea(exceptionText);
    	textArea.setEditable(false);
    	textArea.setWrapText(true);

    	textArea.setMaxWidth(Double.MAX_VALUE);
    	textArea.setMaxHeight(Double.MAX_VALUE);
    	GridPane.setVgrow(textArea, Priority.ALWAYS);
    	GridPane.setHgrow(textArea, Priority.ALWAYS);

    	GridPane expContent = new GridPane();
    	expContent.setMaxWidth(Double.MAX_VALUE);
    	expContent.add(label, 0, 0);
    	expContent.add(textArea, 0, 1);

    	// Set expandable Exception into the dialog pane.
    	this.getDialogPane().setExpandableContent(expContent);
	}
	/**
	 * 
	 */
	public void showAlert() {
		this.showAndWait();
    	System.exit(0);
	}

	
}
