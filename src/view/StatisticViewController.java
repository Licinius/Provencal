package view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;

import controller.MainApp;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import model.Class;
import model.Classes;

public class StatisticViewController {
	@FXML
	private Label percentageQuestion;
	@FXML
	private Label averageLabel;
	@FXML
	private BarChart<String,Integer> barChart;
	
	
	public void initialize() {
		barChart.getXAxis().setLabel("Classes");
		barChart.getYAxis().setLabel("Questions");
	}
	/**
	 * @param classes to know the information to display
	 */
	public void setClasses(MainApp mainApp) {
		Classes classes = mainApp.getClasses();
		NumberFormat formatter = new DecimalFormat("#0.00"); 
		averageLabel.setText(formatter.format(classes.getAverageQuestions()));
		percentageQuestion.setText(formatter.format(mainApp.getProgressProperty().get()*100) + "%");
		
		//Set barChart
		XYChart.Series<String,Integer> series = new XYChart.Series<>();
		for(Class c : classes) {
			series.getData().add(new XYChart.Data<String, Integer>(c.getName(), c.getSizeQuestions()));
		}
		Collections.sort(series.getData(), (o1, o2) -> {//Sort the bars
		    Number xValue1 = (Number) o1.getYValue();
		    Number xValue2 = (Number) o2.getYValue();
		    return new BigDecimal(xValue2.toString()).compareTo(new BigDecimal(xValue1.toString()));
		});
        barChart.getData().add(series);
	}
}
