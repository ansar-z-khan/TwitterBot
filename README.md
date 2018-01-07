# TwitterBot
Trolls a given account by replying to all of it's tweets and replacing a few given words

This code was written in April of last year, it is being uploaded here for storage/visibility


The Twitter bot uses the twitter API to troll a twitter account. Whenever the targeted user tweets, it replis by saying "Did you mean..." [Their original tweet with a few words replaced].

The bot will go through a created CSV file to select keywords saved and it will replace them with words that are decided by the user in the given words.csv 

Set the account you want to reply to by changing the account variable in TwitterBot.java
Enter your own OAuth credentials in TwitterHandler.java in order to be able to tweet 

Example: 

Trump Troll
______________________________
Trump Tweets: "The fake news do not understand the importance of the wall"
Bot Tweets: "Did you mean 'The dank memes do not understand the importance of the taco stand'?"

Shakperian Maker
______________________________
User Tweets: "You look nice today"
Bot Tweets: "Did you mean 'Thou'st look valorius this present day'?"



It creates a CSV file named previousTweets.csv to save the users previous tweets 

Dependencies:
Twitter 4J Library(http://twitter4j.org/en/)
OpenCSV(http://opencsv.sourceforge.net/)
