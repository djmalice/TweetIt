package com.smalltricks.apps.tweetit.fragments;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {
	
	
	public static UserTimelineFragment newInstance(long userId){
		UserTimelineFragment uTimeline= new UserTimelineFragment();
		Bundle args = new Bundle();
		args.putLong("uid", userId);
		uTimeline.setArguments(args);
		return uTimeline;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUserId(getArguments().getLong("uid"));
		tt = timelineType.PROFILE;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		sendJsonRequest(1,-1);
	}
}
