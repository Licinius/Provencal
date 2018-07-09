package factory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Answer;
import model.Question;
import model.User;
import model.Vote;

public class VoteFactory {
	private RemoteFetcher remoteFetcher;
	public VoteFactory() {
		remoteFetcher = new RemoteFetcher();
	}
	
	public VoteFactory(RemoteFetcher remoteFetcher) {
		this.remoteFetcher = remoteFetcher;
	}
	
	public ArrayList<Vote> getVotes(HashMap<Integer, User> users, HashMap<Integer, Answer> answers, HashMap<Integer, Question> questions) throws SQLException {
		ResultSet resultSet = remoteFetcher.fetchAllVotes();
		ArrayList<Vote> votes = new ArrayList<Vote>();
		Vote vote;
		while (resultSet.next()) { 
			vote =  new Vote(
				resultSet.getInt("Id"),
				resultSet.getInt("VoteTypeId"), 
				resultSet.getDate("CreationDate"), 
				resultSet.getInt("BountyAmount"), 
				null, 
				users.get(resultSet.getInt("UserId"))
				);
			if (questions.get(resultSet.getInt("PostId")) != null) { //if the vote is on a question
				vote.setPost(questions.get(resultSet.getInt("PostId")));
			} else if (answers.get(resultSet.getInt("PostId")) != null) { //if the vote is on an answer
				vote.setPost(answers.get(resultSet.getInt("PostId")));
			}
			votes.add(vote);
		}
		return votes;
	}
}
