package com.smalltricks.apps.tweetit.fragments;

import android.os.Bundle;
import android.util.Log;

import com.smalltricks.apps.tweetit.models.Tweet;

public class HomeTimelineFragment extends TweetsListFragment {
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeTimeline = true;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		sendJsonRequest(1,-1);
	}
	
	public void insertComposedTweet(Tweet t){
		Log.d("debug", "Bt3 Current User Details: Name:" + t.getUser().getName());
		Log.d("debug", "Bt3 Current User Details: Handle:" + t.getUser().getScreenName());
		Log.d("debug", "Bt3Current User Details: Imageurl:" + t.getUser().getProfileImageUrl());
		Log.d("debug", "Bt3 Current Tweet Body:" + t.getBody());
		Log.d("debug", "Bt3 Current Tweet Create at:" + t.getCreatedAt());
		aTweets.insert(t, 0);
		aTweets.notifyDataSetChanged();
	}
	
}
