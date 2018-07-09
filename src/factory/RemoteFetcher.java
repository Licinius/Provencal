package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class allows you to execute Query on your DB
 * Need My Sql Connector
 * @author Crauser
 *
 */
public class RemoteFetcher {
	private Statement statement;
	public RemoteFetcher() {
		try {
			String password = "azertyuiop1";
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db_stack?user=root&password="+password+"&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
			this.statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public ResultSet fetchAllUsers() throws SQLException {
		return this.statement.executeQuery(
				"Select * " + 
				"From Users"
				);
	}
	
	public ResultSet fetchAllQuestions() throws SQLException {
		return this.statement.executeQuery(
				"Select * " + 
				"From Posts " + 
				"Where PostTypeId = 1"
				);
	}
	
	public ResultSet fetchAllAnswers() throws SQLException {
		return this.statement.executeQuery(
				"Select * " + 
				"From Posts " + 
				"Where PostTypeId = 2"
				);
	}

	public ResultSet fetchAllComments() throws SQLException {
		return this.statement.executeQuery(
				"Select * " + 
				"From Comments"
				);
	}

	public ResultSet fetchAllBadges() throws SQLException {
		return this.statement.executeQuery(
				"Select * " + 
				"From Badges"
				);
	}

	public ResultSet fetchAllVotes() throws SQLException {
		return this.statement.executeQuery(
				"Select * " + 
				"From Votes"
				);
	}

	public ResultSet fetchAllPostLinks() throws SQLException {
		return this.statement.executeQuery(
				"Select * " + 
				"From PostLinks"
				);
	}

	public ResultSet fetchPostLinksFromTable(String view) throws SQLException {
		return this.statement.executeQuery(
				"Select *" + 
				"From " + view );
	}
}
