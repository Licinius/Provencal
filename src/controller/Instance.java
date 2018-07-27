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
    	/**
    	 * Class constructor initializes collections, and the AchievementManager 
    	 */
    	public Instance() {
    		keyMapping = new HashMap<KeyCode,Class>();
    		questions = new HashMap<Integer,Question>();
    		achievementManager = new AchievementManager();
    		keywords = new HashSet<>();
    	}
    	
    	/**
    	 * @param questions to set in the HashMap
    	 */
    	public void setQuestions(HashMap<Integer,Question> questions) {
    		this.questions = questions;
    	}
    	
    	/**
    	 * 
    	 * @return the size of the questions map
    	 */
    	public int getQuestionCount() {
    		return this.questions.size();
    	}
    	
    	/**
    	 * 
    	 * @return all instance keywords
    	 */
    	public HashSet<String> getKeywords(){
    		return keywords;
    	}
    	
    	/**
    	 * 
    	 * @return the achievement manager allowing to display achievement
    	 */
    	public AchievementManager getAchievementManager() {
    		return achievementManager;
    	}
    	
    	/**
    	 * 
    	 * @return all instance question
    	 */
    	public HashMap<Integer,Question> getQuestions() {
    		return this.questions;
    	}
    	
    	/**
    	 * 
    	 * @return the mapping {@link KeyCode} {@link model.Class }
    	 */
    	public HashMap<KeyCode,Class> getKeyMapping(){
    		return keyMapping;
    	}
    	
    	/**
    	 * Function use to recreate mapping
    	 * @param keyMapping a new mapping {@link KeyCode} {@link model.Class }
    	 */
    	public void setKeyMapping(HashMap<KeyCode,Class> keyMapping) {
    		this.keyMapping = keyMapping;
    	}
    	
    	/**
    	 * 
    	 * @param keywords a HashSet of String representing the keywords 
    	 */
    	public void setKeywords(HashSet<String> keywords) {
    		this.keywords = keywords;
    	}
    	
    	/**
    	 * Save the instance in an ObjectOutputStream if an exception is generate the stackTrace is displayed
    	 * @param filepath to the instance
    	 * @exception IOException can occur if the file can't be found or closed
    	 */
		public void saveInstance(String filepath) throws IOException {
    		FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(filepath);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this);
			objectOutputStream.close();
    	}
		
		/**
		 * Load a previous Instance in a file with the extension .ser
		 * @param filepath to the previous instance
		 * @return an instance load from the file
		 * @throws IOException can occur if the file can't be found or closed
		 * @throws ClassNotFoundException can occur if the class is not found or doesn't exist
		 */
		public Instance loadInstance(String filepath) throws IOException,ClassNotFoundException {
			FileInputStream fileInputStream;
			Instance instance = null;
			fileInputStream = new FileInputStream(filepath);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			instance = (Instance) objectInputStream.readObject();
			objectInputStream.close();
			return instance;
		}
		

		/**
		 * Generate keywords contained in the file in a HashSet of String
		 * @param filepath to the file containing the keywords 
		 * @return an HashSet of String representing the keywords
		 */
		@SuppressWarnings("unchecked")
		public HashSet<String> generateKeywords(String filepath)  {
			HashSet<String> keywords = null;
			try {
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filepath);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
				keywords = (HashSet<String>) objectInputStream.readObject();
				objectInputStream.close();
			} catch (IOException | ClassNotFoundException  e) {
    			e.printStackTrace();
    		}
			return keywords;
		}
    }