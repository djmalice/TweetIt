# Twitter Client Application

This is an Android application providing Twitter like functionality using Twitter API. 

Time spent: 30 hours spent in total

Completed user stories:

 * [x] Required: User can sign in to Twitter using OAuth login
 * [x] Required: User can view the tweets from their home timeline
	* User should be able to see username,name ,body and timestamp
	* User should be displayed realtive timestamp
	* user can view more tweets as they scroll down with infinite pagination
	* Links in tweets are clickable
 * [x] Required: User can compose a new tweet
	* User can click 'Compose' icon in the action bar on top right
	* User can then enter a new tweet and post this to twitter
	* user is taken back to the home timeline with the new tweet visible in timeline
 * [x] Required: User can switch between Timeline and Mention views using tabs.
	* User can view their home timeline tweets.
	* User can view the recent mentions of their username.
	* User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
 * [x] Required: User can navigate to view his own profile
	* User can see picture, tagline, # of followers, # of following, and tweets on their profile.
 * [x] Required: User can click on profile image and see other users' profile
	* User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
	* Profile view should include that user's timeline
	* Optional: User can view following / followers list through the profile
 * [x] Advanced: User can refresh tweets by using pull to refresh
 * [x] Advanced: Open twitter app offline and see last loaded tweets
	* Tweets are persisted in SQLite and can be displayed from local db
 * [x] Advanced: Changes to action bar and icons to make the app look twitter branded
 * [x] Advanced: Check if network is available
 * [x] Advanced: When network request is sent user sees indeterminate progress indicator. 


 
Walkthrough of all user stories:

![Video Walkthrough](tweetIt2.gif)


GIF created with [LiceCap](http://www.cockos.com/licecap/).
