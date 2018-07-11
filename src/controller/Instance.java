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
    	public HashMap<Integer,Question> questions;
    	private int indexQuestions;
    	public Instance() {
    		keyMapping = new HashMap<KeyCode,Class>();
    		questions = new HashMap<Integer,Question>();
    		indexQuestions = 0;
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
    }