package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
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
	private HashSet<Class> classes;
	
	public Classes() {
		classes = new HashSet<Class>();
	}

	/**
	 * Constructor to initialize the classes with a already create ArrayList of classes
	 * @param classes, a ArrayList of Class
	 */
	public Classes(Collection<Class> classes) {
		this.classes = new HashSet<Class>();
		this.classes.addAll(classes);
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
	 * Fired to get the arrayList of classes
	 * @return The current ArrayList of classes
	 */
	public HashSet<Class> getClasses() {
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
	 * This function is called to know how many distinct questions there is in the classes
	 * @return an int that is the number of question with one class
	 */
	public int getQuestionsWithOneClassCount() {
		HashSet<Integer> questionsWithOneClass = new HashSet<>();
		for(Class c : this) {
			for(Question q : c.getQuestions()) {
				questionsWithOneClass.add(q.getId());
			}
		}
		return questionsWithOneClass.size();
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
