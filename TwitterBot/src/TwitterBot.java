import java.util.Date;
import java.util.Scanner;


public class TwitterBot {
	
	/*
	 * Ansar Khan
	 * April 10th, 2017
	 * 
	 * This program is able to troll any twitter account, it takes a users tweets and puts its own spin on them by replacing keywords,
	 * that can be found in the words.csv file. It will look at a users new tweet then tweet back at them if there are any words that need to be replaced
		*Completed Twitter account sign up-DONE
		*Completed Twitter Dev sign up-DONE
		*Completed a program that does a first status update with code-DONE
		*Completed a program that uses a Twitter query
				*Get a user time line instead, still convert array list to array
		*Completed a program that uses arrays to process a Status query result
				*use several arrays
		*Completed a program which uses previously developed methods such as ask() and say() to handle input/output operations.
				*Program does no need input, uses System... print so I can use multiple print streams
				*Uses all the string methods we developed earlier in the year, replace, remove punc, etc..
					*Refer to string Handler class
		*Completed a program of sufficient complexity that acts as a twitter bot
				*Pretty Complex
				*OG Twitter bot, takes Donald TRumps new tweets and corrects it with better words
		*Something else cool	
				*Fully object Oriented, can create new Twitter Handler Classes and troll as many people as I want
					*My first legit object oriented project
				*Created my own custom tweets listener
				*Able to read replace words from a file
				*Programmed a significant part of Parmeet and Shermans Bots 
				*Helped several people with getting the libraries working

	 * */

	public static void main(String[] args)  {
		
		double checkFrequency = 5;//How offen to check for new tweets measured in minutes
		String account = "@realDonaldTrump";//I know i should make this an input, but i get stuck in a chicken/egg conondrum
		String[] newTweets = null;
		TwitterHandler twi = new TwitterHandler (account);
		twi.initLog();
		StringHandler str = new StringHandler();
		
		while (true) {//Loop that periodically runs
			if (twi.newTweet()) {//Checks for new tweet
				try {
				newTweets = twi.getNewTweets();//Gets new Tweets
				}catch (Exception e) {//Catches if they are no new tweets
					System.err.println("Tried to Retreive new tweets, but there were none");
					break;
				}
				for(int i = 0; i<newTweets.length; i++) {//Cycle through every tweet
					if (str.needToReplace(newTweets[i])) {//If there is anything to replace, do it
						twi.tweet(str.replace(newTweets[i]), account);
					}
				}
				twi.initLog();//Update log
			}
			else {
				Date date = new Date(System.currentTimeMillis());
				System.out.println("Checked for Tweet at " + date + " did not find anything");
			}
			try {
				Thread.sleep((long) (checkFrequency*60*1000));//Sleep for a certain amount of time
			} catch (InterruptedException e) {
				System.err.println("Cannot Sleep");
				e.printStackTrace();
			}
		}	
		
	}
	
	
/*
	public static String ask(String prompt, TwitterHandler _twi) {
		String response = "alkhgghlarurgrlegu";
		System.out.println(prompt);
		Scanner input = new Scanner (System.in);
		while (response == "alkhgghlarurgrlegu") {
			try {
				response = input.next();
				_twi.twitter.getUserTimeline();

			} catch (Exception e) {
				System.err.println("Please Enter a Valid Account");
				response = "alkhgghlarurgrlegu";
				input.next();
			}

		}
		return response;
	}
	*/
}



