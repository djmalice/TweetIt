package com.codepath.apps.tweetit;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetit.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
		
	public TweetArrayAdapter(Context context,List<Tweet> tweets) {
		// TODO Auto-generated constructor stub
		super(context,R.layout.tweet_item,tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the data item for the position
		Tweet tweet = getItem(position);
		// Find or inflate the template
		View v;
		if(convertView == null){
			v = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item,parent,false);
		} else {
			v = convertView;
		}
		// Find the views in the template
		ImageView ivProfileImage = (ImageView)v.findViewById(R.id.ivProfileImage);
		ivProfileImage.setImageResource(android.R.color.transparent);
		TextView tvUserName = (TextView)v.findViewById(R.id.tvUserName);
		TextView tvHandle = (TextView)v.findViewById(R.id.tvHandle);
		TextView tvBody = (TextView)v.findViewById(R.id.tvBody);
		TextView tvTimestamp = (TextView)v.findViewById(R.id.tvTimestamp);
		//Populate views with the tweet data
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvUserName.setText(tweet.getUser().getName());
		tvHandle.setText("@" + tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		tvTimestamp.setText(tweet.getTimestamp());
		
		return v;
	}
	
}
