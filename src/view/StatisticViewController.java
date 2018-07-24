package view;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import model.Classes;

public class StatisticViewController {
	@FXML
	private Label percentageQuestion;
	@FXML
	private Label averageLabel;
	@FXML
	private BarChart<Integer,Integer> barChart;
	
	/**
	 * @param classes to know the information to display
	 */
	public void setClasses(Classes classes) {
		averageLabel.setText(Double.toString(classes.getAverageQuestions()));
	}
}
