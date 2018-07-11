package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javafx.scene.input.KeyCode;
import model.Class;
import model.Question;

public class Instance implements Serializable{
		/**
		 * Generated serialVersionUID
		 */
		private static final long serialVersionUID = 5428177751212085034L;
		public HashMap<KeyCode,Class> keyMapping;
    	public HashMap<Integer,Question> remainingQuestions;
    	private int initialQuestionsCount;
    	public Instance() {
    		keyMapping = new HashMap<KeyCode,Class>();
    		remainingQuestions = new HashMap<Integer,Question>();
    	}
    	
		public void saveInstance(String filepath) {
    		FileOutputStream fos;
    		try {
    			fos = new FileOutputStream(filepath);
    			ObjectOutputStream oos = new ObjectOutputStream(fos);
    			oos.writeObject(this);
    			oos.close();
    		} catch (IOException  e) {
    			e.printStackTrace();
    		}
    	}
		
		public Instance loadInstance(String filepath) {
			FileInputStream fileInputStream;
			Instance instance = null;
			try {
				fileInputStream = new FileInputStream(filepath);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				instance = (Instance) objectInputStream.readObject();
				objectInputStream.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Problem while loading...");
			}
			return instance;
		}
		public void setRemainingQuestions(HashMap<Integer,Question> questions) {
			remainingQuestions = questions;
			initialQuestionsCount = questions.size();
		}
		public int getInitialQuestionsCount() {
			return initialQuestionsCount;
		}
    }