package view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;

import achievement.EnumAchievements;
import controller.MainApp;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
	private ProgressIndicator progressIndicator;
	@FXML
	private BarChart<String,Integer> barChart;
	
	private MainApp mainApp;
	final NumberFormat formatter = new DecimalFormat("#0.00"); 
	private  ExecutorService executor;
	public void initialize() {
		barChart.getXAxis().setLabel("Classes");
		barChart.getYAxis().setLabel("Questions");
	}
	
	/**
	 * Set statistics on the current classification, information like distribution or average of keywords will be displayed
	 * @param mainApp the main controller allows to get all the information needed
	 */
	public void setStatistic(MainApp mainApp) {
		this.mainApp = mainApp;
		Classes classes = mainApp.getClasses();
		
		averageLabel.setText(formatter.format(classes.getAverageQuestions()));
		percentageQuestion.setText(formatter.format(mainApp.getProgressProperty().get()*100) + "%");
		
		//Set barChart
		XYChart.Series<String,Integer> series = new XYChart.Series<>();
		for(Class c : classes) {
			series.getData().add(new XYChart.Data<String, Integer>(c.getName(), c.size()));
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
        	if(countKeywords.getValue() != -1) {
	        	keywordsQuestions.setText(formatter.format(countKeywords.getValue()));
	        	progressIndicator.setProgress(1);
	        	progressIndicator.setPrefHeight(19);
	        	
        	}
        	mainApp.getInstance().getAchievementManager().displayAchievement(EnumAchievements.ACH_PATIENCE);
        });
        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
               Thread thread = new Thread(r);
               thread.setDaemon(true);
               return thread;
            }
        });
        executor.submit(countKeywords);
      
	}
	
	public ExecutorService getExecutor() {
		return executor;
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

			/**
			 * This function browse the Element and (recursively)its children and count the number of keyword in the inner html
			 * @param element The Element of the DOM Tree	
			 * @return The number of keyword in the text of a node or its children
			 */
			private int getKeywordCount(Element element) {
				List<Node> children = element.childNodes();
				ArrayList<String> potentialKeywords = new ArrayList<String>();
				Pattern wordPattern;
				int res = 0;
				int wordIndex = 0;
				for(Node n : children) {
					if(!n.nodeName().equals("#text")) {
						res += getKeywordCount((Element) n);
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
			/**
			 * The main of the task, it will loop through the questions and count the keywords to return an average
			 */
			@Override
			protected Double call() throws Exception {
				ArrayList<Question> questions = new ArrayList<>(mainApp.getInstance().getQuestions().values());
				int res = 0;
				int questionCount = 0;
				Iterator<Question> iteratorQuestion = questions.iterator();
				Question question;
				while(iteratorQuestion.hasNext()) {
					if(Thread.currentThread().isInterrupted()) {//If interrupted thread
						return -1d;
					}
					question = iteratorQuestion.next();
					questionCount++;
					String xml = "<body>" + question.getBody() + "</body>";
					Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
					Element body = doc.select("body").get(0);	
					res += getKeywordCount(body);
					if(questionCount%100 == 0) {
						final int resFinal = res;
						final int questionCountFinal = questionCount;
						Platform.runLater(() ->{//Update every hundred
							keywordsQuestions.setText(formatter.format((double)resFinal/questionCountFinal));
						});	
					}
				}			
		        return (double)res/questions.size();
			}		
		};
	}
}
