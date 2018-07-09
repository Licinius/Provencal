package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Registered extends User implements Serializable {
	
	private int id;
	private int reputation;
	private Date creationDate;
	private Date lastAccessDate;
	private String websiteUrl;
	private String location;
	private String aboutMe;
	private int views;
	private int upVotes;
	private int downVotes;
	private String profileImageUrl;
	private String emailHash;
	private int age;
	private int accountId;
	
	private HashMap<Integer,Badge> mapBadge;
	
	//CONSTRUCTOR
	public Registered(String displayName, int id, int reputation, Date creationDate, Date lastAccessDate,
			String websiteUrl, String location, String aboutMe, int views, int upVotes, int downVotes,
			String profileImageUrl, String emailHash, int age, int accountId) {
		super(displayName);
		this.id = id;
		this.reputation = reputation;
		this.creationDate = creationDate;
		this.lastAccessDate = lastAccessDate;
		this.websiteUrl = websiteUrl;
		this.location = location;
		this.aboutMe = aboutMe;
		this.views = views;
		this.upVotes = upVotes;
		this.downVotes = downVotes;
		this.profileImageUrl = profileImageUrl;
		this.emailHash = emailHash;
		this.age = age;
		this.accountId = accountId;
		this.mapBadge = new HashMap<Integer,Badge>();
	}
	
	//GETTER & SETTER
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getUpVotes() {
		return upVotes;
	}

	public void setUpVotes(int upVotes) {
		this.upVotes = upVotes;
	}

	public int getDownVotes() {
		return downVotes;
	}

	public void setDownVotes(int downVotes) {
		this.downVotes = downVotes;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getEmailHash() {
		return emailHash;
	}

	public void setEmailHash(String emailHash) {
		this.emailHash = emailHash;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public HashMap<Integer,Badge> getMapBadge() {
		return mapBadge;
	}

	public void setMapBadge(HashMap<Integer,Badge> mapBadge) {
		this.mapBadge = mapBadge;
	}

	//FUNCTION
	public void addBadge (Badge b) {
		this.mapBadge.put(b.getId(), b);
	}
	
	
	
}
