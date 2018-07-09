package factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Anonymous;
import model.Answer;
import model.Comment;
import model.User;

public class AnswerFactory {
	
	private RemoteFetcher remoteFetcher;
	public AnswerFactory() {
		remoteFetcher = new RemoteFetcher();
	}
	public AnswerFactory(RemoteFetcher remoteFetcher) {
		this.remoteFetcher = remoteFetcher;
	}
	
	public HashMap<Integer, Answer> getAnswers(HashMap<Integer, User> users, ArrayList<Comment> comments) throws SQLException {
		HashMap<Integer, Answer> answers = new HashMap<Integer, Answer>();
		ResultSet resultSet = remoteFetcher.fetchAllAnswers();
		while (resultSet.next()) {
			Answer a = new Answer(
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
				resultSet.getInt("ParentId")
				);		
			//set comment
			for (Comment comment : comments) {
				if (comment.getPostId() == a.getId()) {
					a.addComment(comment);
				}
			}
			
			answers.put(a.getId(), a);
		}
		return answers;
	}
}
