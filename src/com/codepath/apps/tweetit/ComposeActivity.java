package com.codepath.apps.tweetit;

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

import com.codepath.apps.tweetit.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	private TextView tvAccountUserName;
	private TextView tvAccountHandle;
	private EditText etComposeTweet;
	private ImageView ivAccountProfileImage;
	private TwitterClient client;
	
	
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
		data.putExtra("tweetBody", etComposeTweet.getText().toString());
		setResult(RESULT_OK,data);
		finish();
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
