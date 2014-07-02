package com.smalltricks.apps.tweetit.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smalltricks.apps.tweetit.R;
import com.smalltricks.apps.tweetit.TwitterClientApplication;
import com.smalltricks.apps.tweetit.fragments.UserTimelineFragment;
import com.smalltricks.apps.tweetit.models.User;

public class ProfileActivity extends FragmentActivity {
	private TextView tvUserProfileName;
	private TextView tvUserProfileDescription;
	private TextView tvFollowers;
	private TextView tvFollowing;
	private ImageView ivUserProfileImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		tvUserProfileName = (TextView)findViewById(R.id.tvUserProfileName);
		tvUserProfileDescription = (TextView)findViewById(R.id.tvUserProfileDescription);
		ivUserProfileImage = (ImageView)findViewById(R.id.ivUserProfileImage);
		tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		tvFollowing = (TextView)findViewById(R.id.tvFollowing);
		long uid = getIntent().getLongExtra("userid",0);
		
		Log.d("debug", "Uid received from image click" + uid);
		// Populate the frame layout with UserTimeline Fragment
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment uTimeline = UserTimelineFragment.newInstance(uid);
		ft.replace(R.id.flUserTimeline, uTimeline);
		ft.commit();
		
		
		if (uid == 0) {

			TwitterClientApplication.getRestClient().getAppUserProfile(
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject jsonObject) {
							User currentUser = new User();
							try {
								currentUser = User.fromJSON(jsonObject);
								getActionBar().setTitle(
										"@" + currentUser.getScreenName());
								setupUserProfile(currentUser);
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
		} else {
			TwitterClientApplication.getRestClient().getUserProfile(new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(JSONObject jsonObject) {
					User currentUser = new User();
					try {
						currentUser = User.fromJSON(jsonObject);
						getActionBar().setTitle(
								"@" + currentUser.getScreenName());
						setupUserProfile(currentUser);
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
				
				
				
			},uid);
		}
		
		
		
	}
	
	
	private void setupUserProfile(User currentUser){
		if(currentUser !=null){
				tvUserProfileName.setText(currentUser.getName());
				tvUserProfileDescription.setText(currentUser.getDescription());
				tvFollowers.setText(currentUser.getFollowers() + " followers");
				tvFollowing.setText(currentUser.getFollowing() + " following");
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(currentUser.getProfileImageUrl(), ivUserProfileImage);
	
		}
		
	}
	
}
