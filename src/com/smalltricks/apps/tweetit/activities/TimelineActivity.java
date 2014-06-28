package com.smalltricks.apps.tweetit.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.smalltricks.apps.tweetit.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.smalltricks.apps.tweetit.TwitterClient;
import com.smalltricks.apps.tweetit.TwitterClientApplication;
import com.smalltricks.apps.tweetit.adapters.TweetArrayAdapter;
import com.smalltricks.apps.tweetit.helpers.EndlessScrollListener;
import com.smalltricks.apps.tweetit.models.Tweet;
import com.smalltricks.apps.tweetit.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView lvTweets;
	private long reset_max_id;
	private long reset_since_id;
	private final int REQUEST_CODE=20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		reset_max_id=-1;
		client = TwitterClientApplication.getRestClient();
		
		lvTweets = (PullToRefreshListView)findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this,tweets);
		// Log.d("lvArray", aTweets.toString());
		lvTweets.setAdapter(aTweets);
		sendJsonRequest(1,-1);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// TODO Auto-generated method stub
				sendJsonRequest(-1,reset_max_id);
			}
		});
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				fetchNewTweets();
			}
		});
		
		
	}
	
	public void fetchNewTweets(){
		populateFreshTimeline(reset_since_id,0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_item, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void onComposeAction(MenuItem mi){
		Intent i= new Intent(this,ComposeActivity.class);
		startActivityForResult(i,REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		     
		     Tweet t = (Tweet)data.getSerializableExtra("tweet");
		     // Toast the name to display temporarily on screen
		     if (t == null){
		    	 Log.d("debug", "serilaized tweet is null");
		     }else {
		    	Log.d("debug", "Bt3 Current User Details: Name:" + t.getUser().getName());
				Log.d("debug", "Bt3 Current User Details: Handle:" + t.getUser().getScreenName());
				Log.d("debug", "Bt3Current User Details: Imageurl:" + t.getUser().getProfileImageUrl());
				Log.d("debug", "Bt3 Current Tweet Body:" + t.getBody());
				Log.d("debug", "Bt3 Current Tweet Create at:" + t.getCreatedAt());
				aTweets.insert(t, 0);
				aTweets.notifyDataSetChanged();
		        //Toast.makeText(this,t.toString(), Toast.LENGTH_LONG).show();
		     }
		  }
	}
	
	//Check for network connectivity
	private Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	public void sendJsonRequest(long since_id, long max_id){
		if(isNetworkAvailable()){
		   populateTimeline(since_id,max_id);
		} else {
			Log.d("debug", "Network unavailable:");
			List<Tweet> savedTweets = new ArrayList<Tweet>();
			savedTweets = Tweet.getAll();
			
			if(savedTweets != null) {
				Log.d("debug", "dbTweets:" + savedTweets.toString());	
				aTweets.addAll(savedTweets);
			}
			else {
				Toast.makeText(getApplicationContext(), "Check network connectivity", Toast.LENGTH_LONG).show();
			}
			
		}
	}

	public void populateTimeline(long since_id, long max_id){
		Log.d("debug","running populateTimeline" + "since_id: " + since_id + "max_id: " + max_id);
				
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				try { 
					//Log.d("debug",json.toString());
					ArrayList<Tweet> freshTweets = new ArrayList<Tweet>();
					freshTweets = Tweet.fromJSONArray(json);
					ActiveAndroid.beginTransaction();
					try {
					        for (int i = 0; i < freshTweets.size(); i++) {
					            Tweet item = freshTweets.get(i);
					            User u = item.getUser();
					            u.save();
					            item.save();
					        }
					        ActiveAndroid.setTransactionSuccessful();
					}
					finally {
					        ActiveAndroid.endTransaction();
					}
					
					aTweets.addAll(freshTweets);
					reset_max_id = aTweets.getItem(aTweets.getCount()-1).getUid();
					reset_since_id = aTweets.getItem(0).getUid();
								
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
	
	public void populateFreshTimeline(long since_id, long max_id){
		Log.d("debug","running populateFreshTimeline" + "since_id: " + since_id + "max_id: " + max_id);
				
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				try { 
					//Log.d("debug",json.toString());
					ArrayList<Tweet> freshTweets = new ArrayList<Tweet>();
					freshTweets = Tweet.fromJSONArray(json);
					ActiveAndroid.beginTransaction();
					try {
					        for (int i = 0; i < freshTweets.size(); i++) {
					            Tweet item = freshTweets.get(i);
					            User u = item.getUser();
					            u.save();
					            item.save();
					        }
					        ActiveAndroid.setTransactionSuccessful();
					}
					finally {
					        ActiveAndroid.endTransaction();
					}
					for(int i=freshTweets.size()-1;i>=0;i--){
						if(freshTweets.get(i).getUid() > reset_since_id)
							aTweets.insert(freshTweets.get(i), 0);
					}
					reset_max_id = aTweets.getItem(aTweets.getCount()-1).getUid();
					reset_since_id = aTweets.getItem(0).getUid();
					lvTweets.onRefreshComplete();
					
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
