package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import achievement.EnumAchievements;
import controller.MainApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Class;
import model.Question;
/**
 * Controller for the view ChooseClassDialog
 * @author Dell'omo
 *
 */
public class ChooseClassDialogController {
	@FXML
	private BorderPane pane;
	
	private GridPane questionPane;
	private QuestionViewController controllerPane;
	private MainApp mainApp;
	private Question currentQuestion;
	private ArrayList<Question> questions;
	
	private HashSet<Class> potentialClasses;
	private int index;
	
	/**
	 * Init :
	 *  - the handler of key pressed
	 *  - The potential classes to an empty HashSet
	 *  - The index to zero
	 * 
	 */
	public void initialize() {
		pane.setOnKeyPressed(new KeyPressed());
		potentialClasses = new HashSet<>();
		index = 0;
	}
	
	/**
	 * This function return an anchorPane to insert in the gridPane
	 * It also adds the controller and the pane to the Hashmap
	 * @param question a Question to format in a QuestionView
	 * @return The AnchorPane to attach to the GridPane
	 * @throws IOException
	 */
	private GridPane getQuestionPane(Question question,boolean highlight) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/QuestionView.fxml"));
		GridPane pane = loader.load();
		controllerPane = loader.getController();
		controllerPane.setQuestion(question,mainApp.getInstance().getKeywords(),highlight);
		return pane;
	}
	
	/**
	 * This function initialized the dialog with the question in the class
	 * @param mainApp
	 * @param questions
	 * @throws IOException
	 */
	public void initDialog(MainApp mainApp) throws IOException {
		this.mainApp = mainApp;
		this.questions = new ArrayList<>(mainApp.getInstance().getQuestions().values());
		currentQuestion = nextUnclassifiedQuestion();
		if(currentQuestion == null)
			currentQuestion = questions.get(0);
		updateView();
	}
	
	/**
	 * Return the next unclassified question
	 * @return a question if the index is not out of bound
	 */
	public Question nextUnclassifiedQuestion() {
		Question question;
		int previousIndex = index;
		while(index<questions.size()) {
    		question = questions.get(index);
    		if(!question.isClassified())
    			return question;
			index++;
    	}
		index = previousIndex;
		return currentQuestion;
	}
	
	/**
	 * Return the previous unclassified question
	 * @return a question if the index is not out of bound
	 */
	public Question previousUnclassifiedQuestion() {
		Question question;
		int previousIndex = index;
		while(index>0) {
			index--;
    		question = questions.get(index);
    		if(!question.isClassified())
    			return question;
    	}
		index = previousIndex;
		return currentQuestion;
	}
	/**
	 * Return the next question
	 * @return a question if the index is not out of bound
	*/
	public Question nextQuestion() {
		return (index<questions.size()-1?questions.get(++index):currentQuestion);
	}
	
	/**
	 * Return the previous question
	 * @return a question if the index is not out of bound
	 */
	public Question previousQuestion() {
		return ((index<=questions.size() && index>0)?questions.get(--index):currentQuestion);
	}
	/**
	 * Update the view with a new question, it will clear the potentialClasses and remove the question pane before adding the pane
	 */
	public void updateView() {
		potentialClasses.clear();
		pane.getChildren().remove(questionPane);
		boolean previousToggleState = (controllerPane != null?controllerPane.getStateToggleHighLight():true);
		potentialClasses.addAll(currentQuestion.getClasses());
		try {
			questionPane = getQuestionPane(currentQuestion,previousToggleState);
		} catch (IOException e) {
			new AlertException(e).showAlert();
		}
		pane.setCenter(questionPane);
	}
	/**
	 * Event handler to read each keyPressed
	 * @author Dell'omo
	 *
	 */
	private class KeyPressed implements EventHandler<KeyEvent>{

		@Override
		public void handle(KeyEvent arg0) {
			if(mainApp.getInstance().getKeyMapping().containsKey(arg0.getCode())) {
				Class choosenClass = mainApp.getInstance().getKeyMapping().get(arg0.getCode());
				if(potentialClasses.contains(choosenClass)) {
					potentialClasses.remove(choosenClass);
				}else {
					potentialClasses.add(choosenClass);
				}
			}
			switch(arg0.getCode()) {
				case UP:
					index=questions.size()-1;
					currentQuestion = questions.get(index);
					updateView();
					break;
				case DOWN:
					index=0;
					currentQuestion = questions.get(index);
					updateView();
					break;
				case RIGHT:
					potentialClasses.clear();
					currentQuestion = nextQuestion();
					updateView();
					break;
				case LEFT:
					potentialClasses.clear();
					currentQuestion = previousQuestion();
					updateView();
					break;
				case DELETE : 
					if(!currentQuestion.getClasses().isEmpty()) {
						Iterator<Class> iteratorClasses = currentQuestion.getClasses().iterator();
						while(iteratorClasses.hasNext()) {
							Class classToRemove = iteratorClasses.next();
							classToRemove.remove(currentQuestion);
							iteratorClasses.remove();
						}
						updateView();
						mainApp.updateProgress(-1);
					}
					break;
				case PAGE_UP:
					currentQuestion = nextUnclassifiedQuestion();
					updateView();
					break;
				case PAGE_DOWN:
					currentQuestion = previousUnclassifiedQuestion();
					updateView();
					break;
				case F1:
					mainApp.displayMapping();
					break;
				case F2:
					mainApp.getHostServices().showDocument(currentQuestion.getUrl());
					break;
				case ENTER:
					if(!currentQuestion.isClassified()) {
						for(Class choosenClass : potentialClasses) {
							choosenClass.add(currentQuestion);
							currentQuestion.addClass(choosenClass);
						}
						Question questionToDisplay = nextUnclassifiedQuestion();
						if(!potentialClasses.isEmpty()) {
							mainApp.updateProgress(1);
							if(questionToDisplay==currentQuestion) {
								questionToDisplay = previousUnclassifiedQuestion();
								if(questionToDisplay == currentQuestion) {
									mainApp.getInstance().getAchievementManager().displayAchievement(EnumAchievements.ACH_FINISH_CLASS);
								}
							}
							currentQuestion=questionToDisplay;
						}
						updateView();
					}
					break;
				default:
					if(!mainApp.getInstance().getKeyMapping().containsKey(arg0.getCode()))
							mainApp.getInstance().getAchievementManager().displayAchievement(EnumAchievements.ACH_WHAT_DOES_THIS_BUTTON_DO);
					break;
			}
		}
	}
	

}
