package factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import model.Registered;
import model.User;

public class UserFactory {
	
	private RemoteFetcher remoteFetcher;
	public UserFactory() {
		remoteFetcher = new RemoteFetcher();
	}
	
	public UserFactory(RemoteFetcher remoteFetcher) {
		this.remoteFetcher = remoteFetcher;
	}
	
	public HashMap<Integer, User> getAllUsers() throws SQLException {
		HashMap<Integer, User> users = new HashMap<Integer, User>();
		ResultSet resultSet = remoteFetcher.fetchAllUsers();
		while (resultSet.next()) { 
			Registered registeredUser = new Registered(
				resultSet.getString("DisplayName"),
				resultSet.getInt("Id"),
				resultSet.getInt("Reputation"), 
				resultSet.getDate("CreationDate"), 
				resultSet.getDate("LastAccessDate"), 
				resultSet.getString("WebsiteUrl"), 
				resultSet.getString("Location"), 
				resultSet.getString("AboutMe"), 
				resultSet.getInt("Views"), 
				resultSet.getInt("Upvotes"), 
				resultSet.getInt("DownVotes"), 
				resultSet.getString("ProfileImageUrl"), 
				resultSet.getString("EmailHash"), 
				resultSet.getInt("Age"), 
				resultSet.getInt("AccountId")
				);
			
			users.put(registeredUser.getId(), registeredUser); 
		}
		return users;
	}
}
