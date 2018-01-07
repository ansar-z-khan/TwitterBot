	import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

public class StringHandler {

	
	String defaultFileName = "words.csv";
	String[] keyWords;
	String[] replaceWords;
	private String puncHolder = "";
	private int indexOfPunc = 0;
	private String replacePrefix = "Did you mean... ";
	
	CSVReader reader;

	int minReplaceThreshold = 1;
	
	// Constructor: Send in file where keywords are stored
	public StringHandler(String _fileName) {
		try {
			//Tries to create reader to read file
			reader = new CSVReader(new FileReader(_fileName));
		} catch (FileNotFoundException e) {
			//Catches if file does not exist and.or open
			System.err.println("Cannot Access Word File");
			e.printStackTrace();
		}
		try {
			//Populates Word arrays
			keyWords = reader.readNext();
			replaceWords = reader.readNext();
		} catch (IOException e) {
			System.err.println("Cannot Read Word File");
		}

	}

	// Constructor: uses default file name
	public StringHandler() {
		try {
			//Tries to create reader to read file
			reader = new CSVReader(new FileReader(defaultFileName));
		} catch (FileNotFoundException e) {
			//Catches if file does not exist and or open
			System.err.println("Cannot Access Word File");
			e.printStackTrace();
		}
		try {
			keyWords = reader.readNext();
			replaceWords = reader.readNext();
		} catch (IOException e) {
			System.err.println("Cannot Read Word File");
		}

	}
	
	
	//Returns true if a string is all caps
	public boolean isCaps (String word) {
		char[] chars = word.toCharArray();//puts input into char array
		int count = 0;//Creates a variable to count number of uppercase
		for (int i = 0; i<chars.length; i++) {
			if (Character.isUpperCase(chars[i])) {
				count++;
			}
		}
		return count>= chars.length;//Returns true if all are caps
	}

	//Checks if message is under the charachter limit
	public boolean checkCharLimit (int limit, String msg) {
		return msg.length() < limit;
	}
	
	//removes punctuation
	public String removePunc (String word) {
		String[] punc = {".", "," , ";", "-", "_", "?", "!"};//array of basic punctuation
		for (int i = 0; i<punc.length; i++) {//cycles through all punc
			if (word.contains(punc[i])) {
				puncHolder += punc[i];//holds charachter
				indexOfPunc = word.indexOf(punc[i]); // holds index of punc
				word = word.replace(punc[i], "");//removes punctuation
			}
		}
		return word;
	}
	
	//inserts a string in a certain index
	public String insertString(String word, String input, int index) {
		return word.substring(0,index) + input + word.substring (index, word.length());
	}
	
	//puts punctuation back in
	public String replacePunc(String word) {
		indexOfPunc = word.length();
		word = insertString (word, puncHolder, indexOfPunc);
		puncHolder = "";
		indexOfPunc = 0;
		return word;
		
	}
	
	

	
	public String replace (String tweet) {
		String[] words = tweet.split(" ");
		tweet = replacePrefix;
        for(int i = 0; i < words.length; i++) {
        	words[i] = removePunc(words[i]);
            for (int j = 0; j < keyWords.length; j++) {
                if(words[i].equalsIgnoreCase(keyWords[j])) {
                	if (isCaps (words[i])) {
                		 words[i] = replaceWords[j].toUpperCase();
                	}
                	else{
                		words[i] = replaceWords[j];
                	}
                }
            }
           words[i] = replacePunc(words[i]);
           tweet = tweet + " " + words [i];
        }
        return tweet;
	}
	public int CountReplaceWords (String tweet) {
		String[] words = tweet.split(" ");
		int count = 0;
		tweet = "Did You Mean...";
        for(int i = 0; i < words.length; i++) {
        	words[i] = removePunc(words[i]);
            for (int j = 0; j < keyWords.length; j++) {
                if(words[i].equalsIgnoreCase(keyWords[j])) {
                	count++;
                }
            }
        }
        return count;
	}
	public boolean needToReplace(String word) {
		return CountReplaceWords(word)>= minReplaceThreshold;
	}
	
	
	
}
