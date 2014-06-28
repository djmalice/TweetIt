package com.smalltricks.apps.tweetit.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweet")
public class Tweet extends Model implements Serializable  {
	
	private static final long serialVersionUID = -9115245570155496990L;
	@Column(name = "body")
	private String body;
	@Column(name = "uid", unique =true, onUniqueConflict = Column.ConflictAction.REPLACE )
	private long uid;
	@Column(name = "created_at")
	private String createdAt;
	@Column(name = "user", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	
	public Tweet(){
		super();
	}
	
	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	
	public void setBody(String body) {
		this.body = body;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static Tweet fromJSON(JSONObject jsonObject){
		Tweet tweet= new Tweet();
		// Extract values from JSON to populate tweet
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tweet;
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		
		
		for(int i = 0;i<jsonArray.length();i++){
			    JSONObject tweetjson = null;
				try {
					tweetjson = jsonArray.getJSONObject(i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Tweet t = Tweet.fromJSON(tweetjson);
				if(t != null){
					tweets.add(t);
				}
		}		
		return tweets;
	}
	public String getTimestamp(){
		
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(getCreatedAt()).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Some parsing to make it Twitter like
		String rmAgo = relativeDate.substring(0,relativeDate.indexOf(" ")+2);
		String compactRelativeDate = rmAgo.replaceAll("\\s+","");
		
		return compactRelativeDate;
	}
	
	public static List<Tweet> getAll(){
		return new Select().from(Tweet.class).orderBy("created_at DESC").execute();
	}
}
