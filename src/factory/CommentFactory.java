package factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Comment;

public class CommentFactory {
	
	private RemoteFetcher remoteFetcher;
	public CommentFactory() {
		remoteFetcher = new RemoteFetcher();
	}
	
	public CommentFactory(RemoteFetcher remoteFetcher) {
		this.remoteFetcher = remoteFetcher;
	}
	public ArrayList<Comment> getAllComments() throws SQLException {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		ResultSet resultSet = remoteFetcher.fetchAllComments();
		while (resultSet.next()) { 
			Comment c = new Comment(
				resultSet.getInt("Id"), 
				resultSet.getInt("Score"), 
				resultSet.getString("Text"), 
				resultSet.getDate("CreationDate"), 
				resultSet.getInt("PostId")
				);
			comments.add(c);
		}
		return comments;
	}
}
