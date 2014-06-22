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
	private long reset_max_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		reset_max_id=-1;
		client = TwitterClientApplication.getRestClient();
		sendJsonRequest(true);
		lvTweets = (ListView)findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this,tweets);
		Log.d("lvArray", aTweets.toString());
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// TODO Auto-generated method stub
				sendJsonRequest(false);
			}
		});
		
		
	}
	
	public void sendJsonRequest(boolean startPage){
		
		if(startPage){
			populateTimeline(1,-1);
		}else{
			populateTimeline(-1,reset_max_id);
			Log.d("debug", "Sendjsonrequest max_id:" + reset_max_id);
		}
			
		
	}

	public void populateTimeline(long since_id, long max_id){
		Log.d("debug","running populateTimeline" + "since_id: " + since_id + "max_id:" + max_id);
				
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				try { 
					//Log.d("debug",json.toString());
					
					aTweets.addAll(Tweet.fromJSONArray(json));
					
					reset_max_id = aTweets.getItem(aTweets.getCount()-1).getUid();
		
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
		},max_id - 1,since_id);
	}
}
