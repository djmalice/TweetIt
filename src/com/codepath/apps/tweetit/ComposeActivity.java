package com.codepath.apps.tweetit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetit.models.Tweet;
import com.codepath.apps.tweetit.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	private TextView tvAccountUserName;
	private TextView tvAccountHandle;
	private EditText etComposeTweet;
	private ImageView ivAccountProfileImage;
	private TwitterClient client;
	private User u;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		tvAccountUserName = (TextView)findViewById(R.id.tvAccountUserName);
		tvAccountHandle = (TextView)findViewById(R.id.tvAccountHandle);
		ivAccountProfileImage = (ImageView)findViewById(R.id.ivAccountProfileImage);
		etComposeTweet = (EditText)findViewById(R.id.etComposeTweet);
		etComposeTweet.setBackgroundResource(android.R.drawable.editbox_dropdown_light_frame);
		client = TwitterClientApplication.getRestClient();
		client.getUserProfile(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject jsonObject) {
				User currentUser = new User();
				try {
					currentUser = User.fromJSON(jsonObject);
				} catch( Exception e){
					e.printStackTrace();
				}
				Log.d("debug", "Current User Details: Name:" + currentUser.getName());
				Log.d("debug", "Current User Details: Handle:" + currentUser.getScreenName());
				Log.d("debug", "Current User Details: Imageurl:" + currentUser.getProfileImageUrl());
				setupUserInfo(currentUser);
				u=currentUser;
			}
					

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.d("debug",arg0.toString());
				Log.d("debug", arg1.toString());
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_compose, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void onTweetAction(MenuItem mi){
		Intent data = new Intent();
		
		// Build tweet object
	    Tweet t = buildTweet();
	    
	    // Post tweet to Twitter
	    postToTwitter();
	    
	    // Put to intent pass to timeline activity
	    
		data.putExtra("tweet",t);
		setResult(RESULT_OK,data);
		finish();
	}
	
	public void postToTwitter(){
		String status = etComposeTweet.getText().toString();
		Log.d("debug", "Posting to twitter" + status);
		client.postTweet(new JsonHttpResponseHandler(){
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				Log.d("debug",arg0.toString());
				Log.d("debug", arg1.toString());
			}
		},status);
	}
	
	public Tweet buildTweet(){
		Tweet t = new Tweet();
		t.setUser(u);
		t.setBody(etComposeTweet.getText().toString());
		t.setUid(1);
		// Getting current system time
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		t.setCreatedAt(sf.format(new Date()));
		Log.d("debug", "Bt Current User Details: Name:" + t.getUser().getName());
		Log.d("debug", "Bt Current User Details: Handle:" + t.getUser().getScreenName());
		Log.d("debug", "BtCurrent User Details: Imageurl:" + t.getUser().getProfileImageUrl());
		Log.d("debug", "Bt Current Tweet Body:" + t.getBody());
		Log.d("debug", "Bt Current Tweet Create at:" + t.getCreatedAt());
		
		
		return t;
	}
	
	public void setupUserInfo(User currentUser){
		if(currentUser !=null){
				tvAccountUserName.setText(currentUser.getName());
				tvAccountHandle.setText("@" + currentUser.getScreenName());
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(currentUser.getProfileImageUrl(), ivAccountProfileImage);
	
		}
		
	}
	
	
	
}
