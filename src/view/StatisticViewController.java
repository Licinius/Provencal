package view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;

import controller.MainApp;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import model.Class;
import model.Classes;
import model.Question;

public class StatisticViewController {
	@FXML
	private Label percentageQuestion;
	@FXML
	private Label averageLabel;
	@FXML
	private Label keywordsQuestions;
	
	@FXML
	private BarChart<String,Integer> barChart;
	
	private MainApp mainApp;
	
	public void initialize() {
		barChart.getXAxis().setLabel("Classes");
		barChart.getYAxis().setLabel("Questions");
	}
	/**
	 * @param classes to know the information to display
	 */
	public void setClasses(MainApp mainApp) {
		this.mainApp = mainApp;
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
        
        //Process count keyword
        Task<Double> countKeywords = averageKeywordsPerQuestion(mainApp.getInstance().getKeywords());
        countKeywords.setOnSucceeded(arg0->{
        	keywordsQuestions.setText(formatter.format(countKeywords.getValue()));
        });
        new Thread(countKeywords).start();
	}
	
	private Task<Double> averageKeywordsPerQuestion(HashSet<String> keywords){
		return new Task<Double>() {
			
			/**
			 * Check if there is a multiword in the words for the word words[wordIndex].
			 * @param words A Array of string
			 * @param wordIndex The index of the word to test
			 * @param potentialKeywords the postential keyword that match the word
			 * @return the size of the biggest keyword
			 */
			private int getSizeMultiword(String[] words, int wordIndex, ArrayList<String> potentialKeywords) {
				int sizeMax = -1;//default value
				ArrayList<String> splitKeyword = new ArrayList<>();
				int index;
				boolean found; //default true and false if the splitkeyword does not match the text
				for(String keyword : potentialKeywords) {
					splitKeyword.clear();
					index = 1;
					found = true;
					for(String s : keyword.split("((?<=[ ])|(?=[ ]))")) {
						splitKeyword.add(s);
					}
					Iterator<String> splitwordIterator = splitKeyword.iterator();
					String splitword = splitwordIterator.next();
					while (splitwordIterator.hasNext()){
						splitword = splitwordIterator.next();
						int indexNextWord = wordIndex+index;
						if(!(indexNextWord<words.length) || !splitword.equalsIgnoreCase(words[indexNextWord])) {
							found = false;
							break;
						}
						index++;
					}
					if(found) 
						sizeMax = (sizeMax<splitKeyword.size()?splitKeyword.size():sizeMax);
				}
				return sizeMax;
			}
			private int averageKeyword(Element element) {
				Element newElement = new Element(element.nodeName());
				List<Node> children = element.childNodes();
				ArrayList<String> potentialKeywords = new ArrayList<String>();
				Pattern wordPattern;
				int res = 0;
				int wordIndex = 0;
				for(Node n : children) {
					if(!n.nodeName().equals("#text")) {
						res += averageKeyword((Element) n);
					}
					else {
						String[] words = n.toString().split("((?<=[^A-z0-9-])|(?=[^A-z0-9-]))");
						while(wordIndex<words.length) {
							potentialKeywords.clear();
							String word = words[wordIndex];
							if(!word.trim().isEmpty()) {
								wordPattern = Pattern.compile("^(?i)\\b" + Pattern.quote(word) + "\\b.*");
								for(String keyword : keywords) {
									if(wordPattern.matcher(keyword).matches())
										potentialKeywords.add(keyword);	
								}
								if(!potentialKeywords.isEmpty()) {
									int sizeMultiword = getSizeMultiword(words,wordIndex,potentialKeywords);
									if(sizeMultiword>0){
										wordIndex+=sizeMultiword-1;
										res++;
									}
								}
							}
							wordIndex++;
						}
					}
				}
				return res;
			}
			@Override
			protected Double call() throws Exception {
				ArrayList<Question> questions = new ArrayList<>(mainApp.getInstance().getQuestions().values());
				int res = 0;
				int questionSize = questions.size();
				int index =0;
				for(Question q : questions) {
					index++;
					String xml = "<body>" + q.getBody() + "</body>";
					Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
					Element body = doc.select("body").get(0);	
					res += averageKeyword(body);
				}
							
		        return (double)res/questions.size();
			}		
		};
	}
}
