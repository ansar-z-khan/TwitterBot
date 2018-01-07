import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler {

	 Twitter twitter;
	 String acc;
	 int numLogTweets = 20;
	 CSVWriter writer;
	 String logName = "previousTweets.csv";
	 int charLimit = 240;
	 long currentID;

	//Constructor: Uses Default log file name
	public TwitterHandler (String account) {
		//Configures Twitter Account
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("P2F4laRhTLiO12mFAqVStHdtW")
		.setOAuthConsumerSecret("awR5vRVNo9YomfiR9gmNU7g64y5FefigZw5n6icYtg1ESB2SsG")
		.setOAuthAccessToken("457118280-ECZLV8rwXxtmnpbAoWXLa3XT9hrRWygxec3UFBMU")
		.setOAuthAccessTokenSecret("mIfvBT20CjkuboA7QZd5Q1kwgTl8AhKe4H0CjyCV6uziC");
		//Creates Instance of Twitter Object
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		//Sets account 
		acc = account;

	}
	//Constructor: Uses Custom log file name
	public TwitterHandler (String account, String _logName) {
		//Sets log file name
		logName = _logName;
		//Configures Twitter Account
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("3kr3riKy1WPp2MHNLfxompeAB")
		  .setOAuthConsumerSecret("O8Pe1z4NAO5HoIyJJfhYrMSCRx8trwIrH5AGNQ2Qv7KbaQvN6y")
		  .setOAuthAccessToken("457118280-j2OjduXmgSEj9Qv6aVavEvQK2bQxrEZnYB4Hog5J")
		  .setOAuthAccessTokenSecret("dPYF1y5sFoQsGcjtyRV4l5ntaPx9khbo6voVfZVJYGg9h");
		//Configures Twitter Account
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		acc = account;
	}
	public TwitterHandler () {
		//Sets log file name
		//Configures Twitter Account
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("")
		  .setOAuthConsumerSecret("")
		  .setOAuthAccessToken("")
		  .setOAuthAccessTokenSecret("");
		//Configures Twitter Account
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

	}
	
	//Saves Tweets to log files
	public void saveTweets (Status[] tweets) {
		writer.writeNext(getTweetStrings(tweets));//Converts Statuses to strings then writes them
		try {//Tries to close writer and actually write everything
			writer.close();
		} catch (IOException e) {//Catches if something goes wrong
			System.err.println("Unable to Close Writer");
			e.printStackTrace();
		}
	}
	
	//Initializes log with current tweets 
	public void initLog () {
		try {
			//Creates an instance of the writer
			writer = new CSVWriter(new FileWriter(logName), ',');//**Doing this will wipe the file**
		} catch (IOException e) {//Catches if unable to create file
			System.err.println("Unable to create file");
			e.printStackTrace();
		}
		//Actually Saves Tweets
		saveTweets (getTweets(acc, numLogTweets));
		System.out.println("Saved Tweets to Log");
	}
	
	//Checks if there is a new tweet
	public boolean newTweet() {
		String [] currentTweets = getTweetStrings (getTweets(acc, numLogTweets));//Gets current tweets on account
		String [] oldTweets = getTweetsFromLog();//Retrieves tweets stored in log
		for (int i = 0; i<currentTweets.length; i++) {//Goes through each tweet and look for a new tweet
			//System.out.println("Current: "+currentTweets[i]);
			//System.out.println("Log: "+oldTweets[i]);
			if (!currentTweets[i].equalsIgnoreCase(oldTweets[i])) {
				return true;
			}
		}
		return false;
	}
	
	//Counts how many new tweets were made since last tested
	public int numNewTweets() {
		int count = 0;
		String [] currentTweets = getTweetStrings (getTweets(acc, numLogTweets));//Gets current tweets on account
		String [] oldTweets = getTweetsFromLog();//Retrieves tweets stored in log
		String oldString = Arrays.toString(oldTweets);
		for(int i = 0; i<numLogTweets; i++) {
			if(!oldString.contains(currentTweets[i])) {
				count++;
			}
			
		}
		return count;
	}
	
	//Returns the actual text of the new tweets **Needs to know how many tweets there actually are**
	public String[] getNewTweets (int numTweets) throws Exception {
		if (numTweets<0) {
			throw new Exception();
		}
		int index = 0;//Stores the index that it last updated
		String [] currentTweets = getTweetStrings (getTweets(acc, numLogTweets));//Gets current tweets on account
		String [] oldTweets = getTweetsFromLog();//Retrieves tweets stored in log
		String oldString = Arrays.toString(oldTweets);
		String[] newTweets = new String[numTweets];//Creates blank string[]
		//boolean newTweet = false;
		for(int i = 0; i<numLogTweets; i++) {
			if(!oldString.contains(currentTweets[i])) {
				newTweets[index] = currentTweets[i];
				index++;
			}
			
		}

		return newTweets;
		
	}
	
	//Returns the new tweets without needing to know how many new tweets
	public String[] getNewTweets () throws Exception {
		return getNewTweets(numNewTweets());
	}
	
	
	//Sends out a tweet at an account
	public void tweet (String text, String account) {
		text = account + " " + text;
		if (checkCharLimit(charLimit,text)) {//Checks if tweet is less than 140
			tweet(text, true);//Tweets for user 
		}
		else {
			String[] tweets;
			tweets = splitTweet(text);
			for (int i = 0; i<tweets.length;  i++) {
				tweet(tweets[i], true);
			}	
		}
	}
	public void tweet (String text, boolean forReal) {
		try {
			twitter.updateStatus(text);//Tries to tweet
			System.out.println("Tweeted: " + text);
		} catch (TwitterException e) {//Catches if something messes up
			System.err.println("Unable to tweet");
			e.printStackTrace();
		}
	}
	
	public void tweet(String text) {//Tweets but makes sure message is short enough
		if (checkCharLimit(140,text)) {//Checks if message is short enough
			tweet(text, true);
		}
		else {//if it's to long it splits it up
			String[] tweets;
			tweets = splitTweet(text);
			for (int i = 0; i<tweets.length;  i++) {
				tweet(tweets[i], true);
			}
		}
		
	}
	
	public static String[] splitTweet(String tweet) {//split at space
		int charLimit = 140;
		int prefixLength = 5;
		int numTweets = (int) Math.ceil(tweet.length()/charLimit)+1;//Determine number of tweets
		String[] tweets = new String [numTweets];//construct array of tweets
		int indexOfChar; //Create a variable to store where to split number 
		int initChar = 0; //Used to remember where last tweet was split
		
		for (int i = 0; i<tweets.length-1;i++) {
			indexOfChar = i*charLimit-prefixLength+(charLimit-prefixLength-1);//Set index of where to split
			while (tweet.charAt(indexOfChar) != ' ' && indexOfChar>0) {//Keep decreasing index until a space is found
				indexOfChar--;
			}
			tweets[i] = tweet.substring(initChar, indexOfChar );//split tweet
			initChar = indexOfChar+1;//stroe where to start next time
			
		}
		tweets [tweets.length-1] = tweet.substring (initChar, tweet.length()); //Make last tweet from where was left off to end
		
		for (int i = 0; i<tweets.length; i++) {//Adds prefixes to all tweets
			String prefix = "(" + (i+1) + "/" + tweets.length + ")";
			tweets[i] = prefix + tweets[i];
		}
		
		return tweets;
	}
	
	//Returns tweets that were stored in log
	public String[] getTweetsFromLog() {
		CSVReader reader = null;//Declares CSV reader
		 try {
			reader = new CSVReader(new FileReader(logName));//Tries to construct reader, be accessing file
		} catch (FileNotFoundException e) {
			System.err.println("Cannot Retreive Log File");
			e.printStackTrace();
		}
		 try {
			return reader.readNext();//Reads first row of CSV, which is where the tweets are
		} catch (IOException e) {
			System.err.println("Cannot Read Log File");
			e.printStackTrace();
		}
		 return new String[0];
	}
	
	//Gets tweets from an account based on a specified value
	public Status[] getTweets(String account, int numTweets){
		Paging paging = new Paging(1, numTweets);//Creates a paging, which determines how many tweets to get
		try {
			List<Status> tweetsList = twitter.getUserTimeline(acc,paging);//Gets tweets in array list
			Status[] tweetsArr = new Status [tweetsList.size()];//Prepares array for tweets to go into
			tweetsArr = tweetsList.toArray(tweetsArr);//puts tweeets in array
			return (tweetsArr); 
		} catch (TwitterException e) {//Catches of twitter cannot get tweets
			System.err.println("Could not Retrieve tweets");
			e.printStackTrace();
			return new Status[0];
			
		}
	}
	
	//Gets just the text from an array of status objects
	public  String[] getTweetStrings (Status[] tweets) {
		String[] text = new String[tweets.length];//Creates array
		for (int i = 0; i<text.length;i++) {
			text[i] = tweets[i].getText();//GEts text of every tweet
		}
		return text;
	}
	
	public boolean checkCharLimit (int limit, String msg) {
		return msg.length() < limit;
	
	}




}
