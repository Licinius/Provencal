package factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Anonymous;
import model.Answer;
import model.Comment;
import model.Question;
import model.User;
/**
 * Factory for the questions of Stackoverflow
 * @author Crauser
 *
 */
public class QuestionFactory {

	private RemoteFetcher remoteFetcher;
	public QuestionFactory() {
		remoteFetcher = new RemoteFetcher();
	}
	public QuestionFactory(RemoteFetcher remoteFetcher) {
		this.remoteFetcher = remoteFetcher;
	}
	/**
	 * Return all the questions in the database with the users,answers and comments 
	 * @param users 
	 * @param comments
	 * @param answers
	 * @return
	 * @throws SQLException
	 */
	public HashMap<Integer, Question> getAllQuestions(HashMap<Integer, User> users, ArrayList<Comment> comments, HashMap<Integer, Answer> answers) throws SQLException {
		HashMap<Integer, Question> questions = new HashMap<Integer, Question>();
		ResultSet resultSet = remoteFetcher.fetchAllQuestions();
		Question question;
		while (resultSet.next()) {
			question = new Question(
				resultSet.getInt("Id"),
				resultSet.getDate("CreationDate"),
				resultSet.getDate("DeletionDate"),
				resultSet.getInt("Score"),
				resultSet.getString("Body"), 
				resultSet.getDate("LastEditDate"),
				resultSet.getDate("ClosedDate"),
				resultSet.getDate("CommunityOwnedDate"),
				resultSet.getInt("OwnerUserId")==0 ? new Anonymous(resultSet.getString("OwnerDisplayName")) : users.get(resultSet.getInt("OwnerUserId")),  
				resultSet.getInt("LastEditorUserId")==0 ? null : users.get(resultSet.getInt("LastEditorUserId")),
				resultSet.getString("Title"),
				resultSet.getInt("FavoriteCount"),
				resultSet.getInt("ViewCount"),
				resultSet.getString("Tags").replaceAll("&lt;", "").split("&gt;")
				);
			
			//set answers
			int idSelectedAnswer = resultSet.getInt("AcceptedAnswerId");
			for (Answer answer : answers.values()) {
				if (answer.getParentId() == question.getId()) {
					question.addAnswer(answer);
					if (answer.getId() == idSelectedAnswer) {
						question.setSelectedAnswer(answer);
					}
				}
			}
			
			//set comments
			for (Comment comment : comments) {
				if (comment.getPostId() == question.getId()) {
					question.addComment(comment);
				}
			}
			
			questions.put(question.getId(), question);
		}
		return questions;
	}

	
	public HashMap<Integer, Question> getAllSqlQuestions() {
		UserFactory userFactory = new UserFactory(remoteFetcher);
		CommentFactory commentFactory = new CommentFactory(remoteFetcher);
		AnswerFactory answerFactory = new AnswerFactory(remoteFetcher);
		try {
			HashMap<Integer, User> users = userFactory.getAllUsers();
			ArrayList<Comment> comments = commentFactory.getAllComments();
			HashMap<Integer, Answer> answers = answerFactory.getAnswers(users, comments);
			return this.getAllQuestions(users, comments, answers);
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<Integer, Question> getAllSerializedQuestions(String filepath){
		FileInputStream fileInputStream;
		HashMap<Integer, Question> result = null;
		try {
			fileInputStream = new FileInputStream(filepath);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			result = (HashMap<Integer, Question>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (IOException | ClassNotFoundException e ) {
			e.printStackTrace();
		}
		return result;
	}
	
}


