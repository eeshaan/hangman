
/**
 * Main.java created created by eeshaan on Dell Inspiron 7352 in hangman working directory
 * 
 * Description: 
 * 
 * Author:   Eeshaan ([first_name]@cs.wisc.edu)
 * Date:     Jan 22, 2020
 * 
 * IDE:      Eclipse IDE for Java Developers
 * Version:  2019-09
 * Build ID: 1:4.11-3.fc31
 *
 * Device:   purplemachine
 * OS:       Fedora release 31 (Thirty One) x86_64
 * Version:  31
 * Kernel:   5.4.7-200.fc31.x86_64
 * 
 * Other Credits: 1) https://en.wikipedia.org/wiki/ANSI_escape_code — Used for ANSI escape codes
 * 2) https://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java — Used for DecimalFormat
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main - The main class to define the hangman game.
 * 
 * @author Eeshaan ([first_name]@cs.wisc.edu)
 *
 */
public class Main {

	/**
	 * BodyPart - Nested class to provide information about body parts and their
	 * respective coordinates on the char[][] game board
	 * 
	 * @author Eeshaan ([first_name]@cs.wisc.edu)
	 *
	 */
	static class BodyPart {
		int x; // x-coordinate of the char
		int y; // y-coordinate of the char
		char part; // char to represent a body part

		/**
		 * Constructor to define objects of this nested class
		 * 
		 * @param x    - x-coordinate of the char
		 * @param y    - y-coordinate of the char
		 * @param part - char to represent a body part
		 * 
		 */
		public BodyPart(int x, int y, char part) {
			this.x = x;
			this.y = y;
			this.part = part;
		}

		/**
		 * Accessor method for the x-coordinate of a BodyPart object
		 * 
		 * @return the x-coordinate of the BodyPart object
		 * 
		 */
		public int getX() {
			return x;
		}

		/**
		 * Accessor method for the y-coordinate of a BodyPart object
		 * 
		 * @return the y-coordinate of the BodyPart object
		 * 
		 */
		public int getY() {
			return y;
		}

		/**
		 * Accessor method for the relevant char of a BodyPart object
		 * 
		 * @return the relevant char of the BodyPart object
		 * 
		 */
		public char getPart() {
			return part;
		}
	}

	private static final Scanner sc = new Scanner(System.in); // Scanner to get user's keyboard input
	private static PrintWriter pw; // PrintWriter to write output to a .txt file

	/**
	 * Main method to run the game and its relevant helper methods
	 * 
	 * @param args - Arbitrary String arguments to be passed into the method
	 * @throws FileNotFoundException - when createOutput() throws a
	 *                               FileNotFoundException
	 * 
	 */
	public static void main(String[] args) throws FileNotFoundException {

		printWelcome();

		String inputFileName = "";
		File inputFile;
		Scanner fileScan;
		while (true) {
			try {
				System.out.print("\nPlease enter the file name of your desired word list: ");
				inputFileName = sc.next().trim();

				inputFile = new File("/wordlists/" + inputFileName);
				fileScan = new Scanner(inputFile);
				break; // break the loop if the file was found
			} catch (FileNotFoundException e) {
				System.out.println("ERROR: The file was not found.");
				continue; // continue the loop if the file was not found
			}
		}

		ArrayList<String> wordList = new ArrayList<String>();
		
		while (fileScan.hasNext()) {
			wordList.add(fileScan.next()); // populate an ArrayList with all of the words from the given .txt file
		}

		fileScan.close();

		System.out.println("\n" + inputFileName + " was selected as the word list.\nHave fun!\n");

		char noosePic[][] = { { ' ', ' ', ' ', ' ', ' ', '_', '_', '_', '_', '_', '_' },
				{ ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', '|' },
				{ ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'G', 'u',
						'e', 's', 's', 'e', 'd', ':' },
				{ ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
						' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', '_', '_', '|', '_', '_', '_', '_', ' ' },
				{ ' ', ' ', ' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', '|', '_', '_', '_' },
				{ ' ', ' ', ' ', '|', '_', '_', '_', '_', '_', '_', '_', '_', '_', '|' } }; // ASCII art of a hangman
																							// noose stored as a 2D char
																							// array for the game board

		ArrayList<BodyPart> partPositions = new ArrayList<BodyPart>(); // ArrayList of the BodyPart class to store body
																		// part chars and their respective coordinates
																		// on the board
		partPositions.add(new BodyPart(2, 10, 'O'));
		partPositions.add(new BodyPart(3, 10, '|'));
		partPositions.add(new BodyPart(4, 10, '|'));
		partPositions.add(new BodyPart(5, 9, '/'));
		partPositions.add(new BodyPart(3, 9, '/'));
		partPositions.add(new BodyPart(3, 11, '\\'));
		partPositions.add(new BodyPart(5, 11, '\\'));

		printGameBoard(noosePic);

		String wordToGuess = getWord(wordList); // select a random word from the ArrayList

		char[] wordObfuscated = new char[wordToGuess.length()];
		for (int i = 0; i < wordObfuscated.length; i++) {
			wordObfuscated[i] = '-'; // fill the char array with a - to represent each missing letter
		}

		ArrayList<Character> lettersGuessed = new ArrayList<Character>();
		int guessCounter = 0;
		int correctCounter = 0;
		int incorrectCounter = 0;

		while (!String.valueOf(wordObfuscated).equals(wordToGuess)) { // loop will run until the wordObfuscated array
																		// equals wordToGuess
			if (incorrectCounter == 7) { // break loop after losing
				System.out.println("\033[31;1m\n YOU LOSE!\033[0m");
				System.out.println(" The word was \"" + wordToGuess + "\".");
				break;
			}

			System.out.println("\n Word: " + String.valueOf(wordObfuscated));

			System.out.print("\n Guess a letter: ");
			String next = sc.next().toLowerCase();
			char nextChar = next.charAt(0);

			if (next.equals("quit"))
				break;

			if (next.equals("random")) {
				nextChar = (char) ((Math.random() * 27) + 97); // select a random lowercase letter
				System.out.println(" \'" + nextChar + "\' was selected as the random letter.");
			}

			boolean valid = false;

			if (!Character.isLetter(nextChar)) {
				System.out.println(" \'" + nextChar + "\' is not a valid letter.");
			} else if (lettersGuessed.contains(nextChar)) {
				System.out.println(" You've already guessed " + "\'" + nextChar + "\'.");
				valid = false;
			} else if (!lettersGuessed.contains(nextChar)) {
				lettersGuessed.add(nextChar);
				lettersGuessed.sort(Character::compareTo); // sort the ArrayList alphabetically

				for (int i = 18; i < 18 + lettersGuessed.size(); i++) {
					noosePic[3][i] = lettersGuessed.get(i - 18); // add all of the guessed letters to the char[][] game
																	// board
				}

				valid = true;
				guessCounter++;
			}

			boolean correct = false;

			if (valid) {
				for (int i = 0; i < wordToGuess.length(); i++) {
					if (nextChar == wordToGuess.charAt(i)) {
						wordObfuscated[i] = nextChar;
						correct = true;
					}
				}

				if (!correct) {
					System.out.println("\033[31m " + "\'" + nextChar + "\'" + " is not in the word.\033[0m");

					noosePic[partPositions.get(incorrectCounter).getX()][partPositions.get(incorrectCounter)
							.getY()] = partPositions.get(incorrectCounter).getPart(); // add a BodyPart char to the
																						// char[][] game board
					incorrectCounter++;
				} else {
					correctCounter++;
				}
			}

			printGameBoard(noosePic);
		}

		if (String.valueOf(wordObfuscated).equals(wordToGuess)) {
			System.out.println("\033[33;1m\n You got it!\033[0m");
			System.out.println(" The word was \"" + wordToGuess + "\".");
		}

		System.out.println("\n Thanks for playing!");

		sc.close();

		// calls a method that creates a new PrintWriter, writes to it, and closes it
		createOutput(wordToGuess, noosePic, wordObfuscated, guessCounter, correctCounter, incorrectCounter);

		System.out.println(" A summary of this round was generated as \"output.txt\" in the current directory.\n\n");
	}

	/**
	 * Helper method to print a basic intro message using ANSI formatting
	 * 
	 */
	private static void printWelcome() {
		System.out.println("\033[31;1mWelcome to Hangman!\033[0m"); // ANSI escape codes for text styles in terminal
																	// output
		System.out.println("\033[36mType \"quit\" to exit at any time.\033[0m");
		System.out.println("\033[33mType \"random\" to choose a random letter.\033[0m");
	}

	/**
	 * Gets a random word from the given ArrayList
	 * 
	 * @return a random String from the given ArrayList
	 * 
	 */
	private static String getWord(ArrayList<String> wordList) {
		return wordList.get((int) (Math.random() * wordList.size()));
	}

	/**
	 * Prints a 2D char[][] by iterating through it
	 * 
	 * @param gameBoard - the given char[][]
	 * 
	 */
	private static void printGameBoard(char[][] gameBoard) {
		for (int x = 0; x < gameBoard.length; x++) {
			for (int y = 0; y < gameBoard[x].length; y++) {
				System.out.print(gameBoard[x][y]);
			}
			System.out.println();
		}
	}

	/**
	 * Invokes a new PrintWriter and writes to a new files called output.txt with a
	 * summary of the program.
	 * 
	 * @param wordToGuess    - word selected to guess
	 * @param gameBoard      - char[][] of the final game board
	 * @param finalWord      - the final representation of the word as a char[]
	 * @param guessCount     - the total number of guesses the player made
	 * @param correctCount   - the number of correct guesses the player made
	 * @param incorrectCount - the number of incorrect guesses the player made
	 * 
	 * @throws FileNotFoundException - when output.txt cannot be generated by the
	 *                               new PrinterWriter
	 * 
	 */
	private static void createOutput(String wordToGuess, char[][] gameBoard, char[] finalWord, int guessCount,
			int correctCount, int incorrectCount) throws FileNotFoundException {

		pw = new PrintWriter("output.txt");

		pw.write("Welcome to output.txt — a file generated by this program.");
		pw.write("\n—————————————————————————————————————————————————————————");
		pw.write("\n\nThe last time hangman was played, \"" + wordToGuess + "\" was selected as the word to guess.");
		pw.write("\n\nHere's what the final game board looked like:\n");

		for (int x = 0; x < gameBoard.length; x++) {
			for (int y = 0; y < gameBoard[x].length; y++) {
				pw.write(gameBoard[x][y]);
			}
			pw.write("\n");
		}

		pw.write("\n Word: " + String.valueOf(finalWord));

		String gameResult = (String.valueOf(finalWord).equals(wordToGuess)) ? "won" : "lost";

		pw.write("\n\n\nThe player " + gameResult + " with a total of " + guessCount + " tries.");
		pw.write("\n\nCorrect Guesses  : " + correctCount);
		pw.write("\nInorrect Guesses : " + incorrectCount);
		pw.write("\nTotal Guesses    : " + guessCount);

		double percentage = ((double) correctCount / (double) guessCount) * 100;
		DecimalFormat df = new DecimalFormat("##.00");

		pw.write("\n\nPercentage of correct guesses: " + df.format(percentage) + "%");

		pw.close();
	}

}
