package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
/**
 * This class is used to get statistic of a set of Class
 * @author Dell'omo
 *
 */
public class Classes implements Serializable,Collection<Class>{
	/**
	 * The generated serialUID
	 */
	private static final long serialVersionUID = 1276237082847124105L;
	ArrayList<Class> classes;
	
	public Classes() {
		classes = new ArrayList<Class>();
	}
	
	/**
	 * This function allows to save easily the classes
	 * @param filepath the name of the file where to save the Classes
	 * @throws IOException Throws a Exception if an exception is raised by the *OutputStream
	 */
	public void save(String filepath) throws IOException{
		FileOutputStream fileOutputStream = new FileOutputStream(filepath);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
	}
	
	/**
	 * This static function allows the user to read a filepath that contain the classes
	 * @param filepath the name of the file where to save the Classes
	 * @return The class read in the file or null
	 * @throws IOException Throws a Exception if an exception is raised by the *OutputStream
	 */
	public static Classes read(String filepath) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(filepath);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		Classes classes;
		try{
			 classes = (Classes) objectInputStream.readObject();
		}
		catch(ClassNotFoundException e) {
			System.err.println("An eror occured while casting the class");
			classes = null;
		}
		objectInputStream.close();
		return classes;
	}
	
	/**
	 * Constructor to initialize the classes with a already create ArrayList of classes
	 * @param classes, a ArrayList of Class
	 */
	public Classes(ArrayList<Class> classes) {
		this.classes = classes;
	}
	
	/**
	 * Fired to get the arrayList of classes
	 * @return The current ArrayList of classes
	 */
	public ArrayList<Class> getClasses() {
		return classes;
	}
	/**
	 * This function is called to know the average number of questions in the classes
	 * @return a double that is the average of questions in the classes 
	 */
	public double getAverageQuestions() {
		int questionsCount = 0;
		for(Class aClass : classes) {
			questionsCount+=aClass.getSizeQuestions();
		}
		return (double)questionsCount/classes.size();
	}
	/**
	 * This function is called to know the average of keywords in the classes
	 * @return a double that is the average of questions in the classes
	 */
	public double getAverageKeywords() {
		int keywordsCount = 0;
		for(Class aClass : classes) {
			keywordsCount += aClass.getKeywords().size();
		}
		return (double)keywordsCount/classes.size();
	}
	/**
	 * This function is called to get the class with the maximum number of keywords
	 * @return a class with the largest number of keyword, if there is no class return null
	 */
	public Class getClassWithMaximumKeyword() {
		int maximumKeywords = 0;
		Class res = null;
		for(Class aClass : classes) {
			if(aClass.getKeywords().size()>maximumKeywords) {
				maximumKeywords = aClass.getKeywords().size();
				res = aClass;
			}
		}
		return res;
	}
	
	@Override
	public Iterator<Class> iterator() {
		return classes.iterator();
	}

	@Override
	public boolean add(Class arg0) {
		return classes.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends Class> arg0) {
		return this.classes.addAll(arg0);
	}

	@Override
	public void clear() {
		this.classes.clear();		
	}

	@Override
	public boolean contains(Object arg0) {
		return this.classes.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return this.classes.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return this.classes.isEmpty();
	}

	@Override
	public boolean remove(Object arg0) {
		return this.classes.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return this.classes.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return this.classes.retainAll(arg0);
	}

	@Override
	public int size() {
		return this.classes.size();
	}

	@Override
	public Object[] toArray() {
		return this.classes.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return this.classes.toArray(arg0);
	}
}
