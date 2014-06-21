package com.codepath.apps.tweetit.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private String body;
	private long uid;
	private String createdAt;
	private User user;
	
	
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getBody() + "-" + getUser().getName() ;
	}
}
