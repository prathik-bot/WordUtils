import java.util.Scanner;

/**
 *	Provides utilities for word games:
 *	1. finds all words in the dictionary that match a list of letters
 *	2. prints an array of words to the screen in tabular format
 *	3. finds the word from an array of words with the highest score
 *	4. calculates the score of a word according to a table
 *
 *	Uses the FileUtils and Prompt classes.
 *	
 *	@author			Prathik Kumar
 *	@since 			October 19, 2023
 */
 
public class WordUtils
{
	private String[] words;		// the dictionary of words
	
	// File containing dictionary of almost 100,000 words.
	private final String WORD_FILE = "wordList.txt";

	private static int wordCount = 0; // used to count the words
	
	/* Constructor */
	public WordUtils() {
		words = new String[90934];
		loadWords();
	}
	
	/**	Load all of the dictionary from a file into words array. */
	private void loadWords() {
		Scanner input = FileUtils.openToRead(WORD_FILE);
		int index = 0;
		while (input.hasNext() && index < words.length) {
			words[index] = input.next();
			index++;
		}
		wordCount = index;
		input.close();
	}
	
	/**	Find all words that can be formed by a list of letters.
	 *  @param letters	string containing list of letters
	 *  @return			array of strings with all words found.
	 */
	public String[] findAllWords(String letters) {
		// Create an array to store matching words.
		String[] matchingWords = new String[wordCount];
		int numMatches = 0;
		for (int i = 0; i < words.length; i ++) {
			if (isWordMatch(words[i].toLowerCase(), letters)) {
				matchingWords[numMatches] = words[i].toLowerCase();
				numMatches++;
			}
		}
		String [] result = new String[numMatches];
		for (int i = 0; i < numMatches; i++) {
			result[i] = matchingWords[i];
		}
		return result;
	}

	/**
	 *  Decides if a word matches a group of letters.
	 *
	 *  @param word  The word to test.
	 *  @param letters  A string of letters to compare
	 *  @return  true if the word matches the letters, false otherwise
	 */
	public boolean isWordMatch (String word, String letters) {
		for(int a = 0; a < word.length(); a++)
		{
			char c  = word.charAt(a);
			if(letters.indexOf(c) > -1)
			{
				letters = letters.substring(0, letters.indexOf(c)) +
				letters.substring(letters.indexOf(c) + 1);
			}
			else
				return false;
		}

		return true;
	}

	/**	Print the words found to the screen.
	 *  @param wordList	array containing the words to be printed
	 */
	public void printWords(String[] wordList) {
		System.out.println();
		int wordsPerLine = 5; // Adjust the number of words per line as needed
		for (int i = 0; i < wordList.length; i++) {
			System.out.printf("%-10s", wordList[i]); // Adjust the spacing as needed
			if ((i + 1) % wordsPerLine == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

	/**	Finds the highest scoring word according to a score table.
	 *
	 *  @param wordList  		An array of words to check
	 *  @param scoreTable	An array of 26 integer scores in letter order
	 *  @return   			The word with the highest score
	 */
	public String bestWord(String[] wordList, int[] scoreTable) {
		String bestWord = "";
		int bestScore = 0;

		for (int i = 0; i < wordList.length; i ++) {
			int wordScore = getScore(wordList[i], scoreTable);
			if (wordScore > bestScore) {
				bestScore = wordScore;
				bestWord = wordList[i];
			}
		}

		return bestWord;
	}
	
	/**	Calculates the score of one word according to a score table.
	 *
	 *  @param word			The word to score
	 *  @param scoreTable	An array of 26 integer scores in letter order
	 *  @return				The integer score of the word
	 */
	public int getScore(String word, int[] scoreTable) {
		int score = 0;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			int index = c - 'a'; // Assuming lowercase letters only.
			score += scoreTable[index];
		}
		return score;
	}
	
	/***************************************************************/
	/************************** Testing ****************************/
	/***************************************************************/
	public static void main (String [] args)
	{
		WordUtils wu = new WordUtils();
		wu.run();
	}
	
	public void run() {
		String letters = Prompt.getString("Please enter a list of letters, from 3 to 12 letters long, without spaces");
		String [] word = findAllWords(letters);
		System.out.println();
		printWords(word);
		
		// Score table in alphabetic order according to Scrabble
		int [] scoreTable = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
		String best = bestWord(word,scoreTable);
		System.out.println("\nHighest scoring word: " + best + "\nScore = " 
							+ getScore(best, scoreTable) + "\n");
	}
}
