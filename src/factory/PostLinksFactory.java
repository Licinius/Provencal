package factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import model.PostLink;
import model.Question;

public class PostLinksFactory {
	private RemoteFetcher remoteFetcher;
	public PostLinksFactory() {
		remoteFetcher = new RemoteFetcher();
	}
	
	public ArrayList<PostLink> getAllPostLinks(HashMap<Integer, Question> questions) throws SQLException {
		ArrayList<PostLink> postLinks = new ArrayList<PostLink>();
		ResultSet resultSet = remoteFetcher.fetchAllPostLinks();
		PostLink postLink;
		while (resultSet.next()) { 
			postLink = new PostLink(
				resultSet.getInt("Id"), 
				resultSet.getDate("CreationDate"), 
				resultSet.getInt("LinkTypeId"), 
				questions.get(resultSet.getInt("PostId")), 
				questions.get(resultSet.getInt("RelatedPostId"))
				);
			postLinks.add(postLink);
		}
		return postLinks;
	}
	/**
	 * Call {@link #getPostLinks(String) getPostLinks} with a the default table parameter
	 * @return an arrayList of PostLink
	 * @throws SQLException 
	 */
	public ArrayList<PostLink> getPostLinks() throws SQLException{
		return getPostLinks("Postlinks");
	}
	/**
	 * This function will create the postLink with dummy Question
	 * @param view
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<PostLink> getPostLinks(String view) throws SQLException{
		ArrayList<PostLink> postLinks = new ArrayList<PostLink>();
		ResultSet resultSet = remoteFetcher.fetchPostLinksFromTable(view);
		PostLink postLink;
		Question post,relatedPost;
		while(resultSet.next()) {
			post = new Question();
			relatedPost = new Question();
			post.setId(resultSet.getInt("PostId"));
			relatedPost.setId(resultSet.getInt("RelatedPostId"));
			postLink = new PostLink(
					resultSet.getInt("Id"), 
					resultSet.getDate("CreationDate"), 
					resultSet.getInt("LinkTypeId"),
					post, 
					relatedPost
			);
			postLinks.add(postLink);
		}
		return postLinks;
	}
}
