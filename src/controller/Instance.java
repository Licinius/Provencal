package controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import achievement.AchievementManager;
import javafx.scene.input.KeyCode;
import model.Class;
import model.Question;

public class Instance implements Serializable{
		/**
		 * Generated serialVersionUID
		 */
		private static final long serialVersionUID = 5428177751212085034L;
		private HashMap<KeyCode,Class> keyMapping;
    	private HashMap<Integer,Question> questions;
    	private AchievementManager achievementManager;
    	private HashSet<String> keywords;
    	
    	public Instance() {
    		keyMapping = new HashMap<KeyCode,Class>();
    		questions = new HashMap<Integer,Question>();
    		achievementManager = new AchievementManager();
    		keywords = new HashSet<>();
    	}
    	public void setQuestions(HashMap<Integer,Question> questions) {
    		this.questions = questions;
    	}
    	public int getQuestionsCount() {
    		return this.questions.size();
    	}
    	public HashSet<String> getKeywords(){
    		return keywords;
    	}
    	public AchievementManager getAchievementManager() {
    		return achievementManager;
    	}
    	public HashMap<Integer,Question> getQuestions() {
    		return this.questions;
    	}
    	public HashMap<KeyCode,Class> getKeyMapping(){
    		return keyMapping;
    	}
    	public void setKeyMapping(HashMap<KeyCode,Class> keyMapping) {
    		this.keyMapping = keyMapping;
    	}
    	public void setKeywords(String[] keywords) {
    		for(String keyword : keywords) {
    			this.keywords.add(keyword);
    		}
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
		
		@SuppressWarnings("unchecked")
		public String[] generateKeywords(String filepath)  {
			String[] keywords = null;
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filepath);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
				keywords = ((String[]) objectInputStream.readObject());
				objectInputStream.close();
			} catch (IOException | ClassNotFoundException  e) {
    			e.printStackTrace();
    		}
			return keywords;
		}
    }