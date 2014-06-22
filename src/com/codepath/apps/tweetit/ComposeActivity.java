package com.codepath.apps.tweetit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetit.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	private TextView tvAccountUserName;
	private TextView tvAccountHandle;
	private EditText etComposeTweet;
	private ImageView ivAccountProfileImage;
	private TwitterClient client;
	private User currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		tvAccountUserName = (TextView)findViewById(R.id.tvAccountUserName);
		tvAccountHandle = (TextView)findViewById(R.id.tvAccountHandle);
		ivAccountProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
		etComposeTweet = (EditText)findViewById(R.id.etComposeTweet);
		
		client = TwitterClientApplication.getRestClient();
		client.getUserProfile(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.d("debug", "Userprofile JSON Object" + jsonObject);
				currentUser = User.fromJSON(jsonObject);
			}
			@Override
			public void onSuccess(JSONArray jsonArray) {
				// TODO Auto-generated method stub
				Log.d("debug", "Userprofile JSON Array" + jsonArray);
			}
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.d("debug",arg0.toString());
				Log.d("debug", arg1.toString());
			}
		});
		
		tvAccountUserName.setText(currentUser.getName());
		tvAccountHandle.setText(currentUser.getScreenName());
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(currentUser.getProfileImageUrl(), ivAccountProfileImage);
	}
}
