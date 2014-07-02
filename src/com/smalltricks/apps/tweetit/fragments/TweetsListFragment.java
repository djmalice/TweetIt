package com.smalltricks.apps.tweetit.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.smalltricks.apps.tweetit.R;
import com.smalltricks.apps.tweetit.TwitterClient;
import com.smalltricks.apps.tweetit.TwitterClientApplication;
import com.smalltricks.apps.tweetit.adapters.TweetArrayAdapter;
import com.smalltricks.apps.tweetit.helpers.EndlessScrollListener;
import com.smalltricks.apps.tweetit.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	protected ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView lvTweets;
	private long reset_max_id;
	private long reset_since_id;
	private long userId;
	/*protected enum timelineType{
		HOME("HOME",0),
		MENTIONS("MENTIONS",1),
		PROFILE("PROFILE",2);
		
		private String stringValue;
		private int intValue;
		
		private timelineType(String toString, int value){
			stringValue = toString;
			intValue = value;
		}
		
		public String toString(){
			return stringValue;
		}
		
		public int getValue(){
			return intValue;
		}
	}*/
	
	

	public void setUserId(long userId) {
		this.userId = userId;
	}

	protected enum timelineType{
		HOME,
		MENTIONS,
		PROFILE
	}
	
	timelineType tt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(),tweets);
		client = TwitterClientApplication.getRestClient();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		// Assign our view references
		// lvItems.......
		lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
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
		
		
		
		
		
		// return layout view
		
		return v;
	}
	
	public void fetchNewTweets(){
		populateFreshTimeline(reset_since_id,0,tt.ordinal());
	}
	
	public void sendJsonRequest(long since_id, long max_id){
		if(isNetworkAvailable()){
		   populateTimeline(since_id,max_id,tt.ordinal());
		} else {
			Log.d("debug", "Network unavailable:");
			List<Tweet> savedTweets = new ArrayList<Tweet>();
//			savedTweets = Tweet.getAll();
			
			if(savedTweets != null) {
				Log.d("debug", "dbTweets:" + savedTweets.toString());
				
				addAll(new ArrayList<Tweet>(savedTweets));
			}
			else {
				Toast.makeText(getActivity(), "Check network connectivity", Toast.LENGTH_LONG).show();
			}
			
		}
	}
	
	public void populateTimeline(long since_id, long max_id, int tid){
		Log.d("debug","running populateTimeline" + "since_id: " + since_id + "max_id: " + max_id);
				
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				try { 
					//Log.d("debug",json.toString());
					ArrayList<Tweet> freshTweets = new ArrayList<Tweet>();
					freshTweets = Tweet.fromJSONArray(json);
				/*ActiveAndroid.beginTransaction();
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
					}*/
					
					addAll(freshTweets);
					
								
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
		},max_id - 1,since_id,tid,userId);
	}
	
	public void populateFreshTimeline(long since_id, long max_id, int tid){
		Log.d("debug","running populateFreshTimeline" + "since_id: " + since_id + "max_id: " + max_id);
				
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray json) {
				try { 
					//Log.d("debug",json.toString());
					ArrayList<Tweet> freshTweets = new ArrayList<Tweet>();
					freshTweets = Tweet.fromJSONArray(json);
					/*ActiveAndroid.beginTransaction();
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
					}*/
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
		},max_id - 1,since_id,tid,userId);
	}
	
	//Check for network connectivity
		private Boolean isNetworkAvailable() {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
		}
		
		
	
	public void addAll(ArrayList<Tweet> tweets){
		aTweets.addAll(tweets);
		reset_max_id = aTweets.getItem(aTweets.getCount()-1).getUid();
		reset_since_id = aTweets.getItem(0).getUid();
	}
	
	
}
