package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import achievement.AchievementManager;
import javafx.scene.input.KeyCode;
import model.Class;
import model.Question;

public class Instance implements Serializable{
		/**
		 * Generated serialVersionUID
		 */
		private static final long serialVersionUID = 5428177751212085034L;
		public HashMap<KeyCode,Class> keyMapping;
    	private HashMap<Integer,Question> questions;
    	private AchievementManager achievementManager;
    	private int questionsCount;
    	public Instance() {
    		keyMapping = new HashMap<KeyCode,Class>();
    		questions = new HashMap<Integer,Question>();
    		achievementManager = new AchievementManager();
    		questionsCount = 0;
    	}
    	public void setQuestions(HashMap<Integer,Question> questions) {
    		this.questions = questions;
    		this.questionsCount = questions.size();
    	}
    	public int getQuestionsCount() {
    		return this.questionsCount;
    	}
    	public AchievementManager getAchievementManager() {
    		return achievementManager;
    	}
    	public HashMap<Integer,Question> getQuestions() {
    		return this.questions;
    	}
		public void saveInstance(String filepath) {
    		FileOutputStream fileOutputStream;
    		try {
    			fileOutputStream = new FileOutputStream(filepath);
    			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    			objectOutputStream.writeObject(this);
    			objectOutputStream.close();
    		} catch (IOException  e) {
    			e.printStackTrace();
    		}
    	}
		
		public Instance loadInstance(String filepath) throws IOException,ClassNotFoundException {
			FileInputStream fileInputStream;
			Instance instance = null;
			fileInputStream = new FileInputStream(filepath);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			instance = (Instance) objectInputStream.readObject();
			objectInputStream.close();
			return instance;
		}
    }