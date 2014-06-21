package com.codepath.apps.tweetit;

import java.util.ArrayList;

import org.json.JSONArray;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.tweetit.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterClientApplication.getRestClient();
		populateTimeline();
		lvTweets = (ListView)findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this,tweets);
		Log.d("lvArray", aTweets.toString());
		lvTweets.setAdapter(aTweets);
		
	}
	
	public void populateTimeline(){
		Log.d("debug","running populateTimeline");
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				try { 
					Log.d("debug",json.toString());
					aTweets.addAll(Tweet.fromJSONArray(json));
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.d("debug",arg0.toString());
				Log.d("debug", arg1.toString());
				
			}
		});
	}
}
