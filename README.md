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
 * [x] Advanced: User can refresh tweets by using pull to refresh
 * [x] Advanced: Open twitter app offline and see last loaded tweets
	* Tweets are persisted in SQLite and can be displayed from local db
* [x] Advanced: Changes to action bar and icons to make the app look twitter branded


Work still needs to be done to get the ordering of tweets right. Currently the ordeing between pull to refresh, pagination and tweets from local db is not perfect. However, all 3 features do work. 
 
Walkthrough of all user stories:

![Video Walkthrough](tweetit.gif)


GIF created with [LiceCap](http://www.cockos.com/licecap/).
