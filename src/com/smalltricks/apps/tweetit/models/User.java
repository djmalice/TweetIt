package com.smalltricks.apps.tweetit.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public class User extends Model implements Serializable{
	private static final long serialVersionUID = -8959832007991513854L;
	@Column( name = "Name")
	private String name;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name = "screenname")
	private String screenName;
	@Column(name = "profileimageurl")
	private String profileImageUrl;
	@Column(name = "Description")
	private String description;
	@Column(name = "followers")
	private long followers ;
	@Column(name = "following")
	private long following;

	public static User fromJSON(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.description = jsonObject.getString("description");
			user.followers = jsonObject.getLong("followers_count");
			user.following = jsonObject.getLong("friends_count");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
	public User(){
		super();
		
	}
	
	public User(User u){
		this.name = u.name;
		this.profileImageUrl = u.profileImageUrl;
		this.screenName = u.screenName;
		this.uid = u.uid;
		this.description = u.description;
		this.followers = u.followers;
		this.following = u.following;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	
	public String getDescription() {
		return description;
	}
	public long getFollowers() {
		return followers;
	}
	
	public long getFollowing() {
		return following;
	}
	
}
