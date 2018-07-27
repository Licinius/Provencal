package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class of a questions with a name and a collection of questions
 * @author Dell'omo
 *
 */
@SuppressWarnings("serial")
public class Class implements Serializable,Collection<Question> {
	
	private String  name;
	private ArrayList<Question> questions;
	
	/**
	 * This constructor initialize a class with a name
	 * @param name to sets
	 */
	public Class(String name) {
		this.name = name;
		this.questions = new ArrayList<Question>();
	}
	
	/**
	 * 
	 * @return the name of the class
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This function is used by the factory {@link view.ListOverviewController}
	 * @return the questions size
	 */
	public int getQuestionCount() {
		return size();
	}
	
	/**
	 * 
	 * @return the questions in the class
	 */
	public ArrayList<Question> getQuestions(){
		return questions;
	}

	@Override
	public boolean add(Question arg0) {
		return questions.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends Question> arg0) {
		return questions.addAll(arg0);
	}

	@Override
	public void clear() {
		questions.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return questions.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return questions.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Question> iterator() {
		return questions.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return questions.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return questions.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return questions.retainAll(arg0);
	}

	@Override
	public int size() {
		return questions.size();
	}

	@Override
	public Object[] toArray() {
		return questions.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return questions.toArray(arg0);
	}
}
