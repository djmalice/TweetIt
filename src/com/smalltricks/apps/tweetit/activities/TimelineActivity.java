package com.smalltricks.apps.tweetit.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.smalltricks.apps.tweetit.R;
import com.smalltricks.apps.tweetit.TwitterClientApplication;
import com.smalltricks.apps.tweetit.fragments.HomeTimelineFragment;
import com.smalltricks.apps.tweetit.fragments.MentionsTimelineFragment;
import com.smalltricks.apps.tweetit.listeners.FragmentTabListener;
import com.smalltricks.apps.tweetit.models.Tweet;
import com.smalltricks.apps.tweetit.models.User;

public class TimelineActivity extends FragmentActivity {
	
	
	private final int REQUEST_CODE=20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupTabs();
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "first",
								HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "second",
								MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
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
	
	public void onProfileView(MenuItem mi){
		TwitterClientApplication.getRestClient().getAppUserProfile(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						User currentUser = new User();
						try {
							currentUser = User.fromJSON(jsonObject);
							startProfileActivity(currentUser);
						} catch (Exception e) {
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
	
	
	public void startProfileActivity(User currentUser){
		Intent i= new Intent(this,ProfileActivity.class);
		i.putExtra("userid", currentUser.getUid());
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		     
		     Tweet t = (Tweet)data.getSerializableExtra("tweet");
		     // Toast the name to display temporarily on screen
		     if (t == null){
		    	 Log.d("debug", "serilaized tweet is null");
		     }else {
		    	 HomeTimelineFragment homeFragment = (HomeTimelineFragment)getSupportFragmentManager().findFragmentById(R.id.flContainer);
		    	 homeFragment.insertComposedTweet(t);
		    	 Toast.makeText(this,t.toString(), Toast.LENGTH_LONG).show();
		     }
		  }
	}
	
	

	
	
	
	
	
	
	
	
}
