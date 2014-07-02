package com.smalltricks.apps.tweetit.fragments;

import android.os.Bundle;

public class MentionsTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tt = timelineType.MENTIONS;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		sendJsonRequest(1,-1);
	}

}
