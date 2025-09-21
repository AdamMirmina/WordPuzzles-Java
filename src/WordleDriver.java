import java.util.HashSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

// Driver for Wordle game; chooses a word, takes in user input, and calls methods of GuessChecker to determine feedback
public class WordleDriver
{
    // NEW CONCEPT: HASHSET
    private static HashSet<String> validWords = new HashSet<>();
    private static HashSet<String> guessable = new HashSet<>();

    private static String word; // Word that user will attempt to guess
    public static String getWord() {
        return word;
    }
    private static int streak; // Streak of the user's victorious Wordle games
    public static int getStreak() {
        return streak;
    }

    private static int longestStreak; // Longest streak that the user has attained
    public static int getLongestStreak() {
        return longestStreak;
    }

    public static boolean playAgain; // Signifies that the user asked to play again

    private static int round = 0; // Counts the current round number
    public static int getRound() {
        return round;
    }

    private static boolean winCondition = false; // Is set to true if user guesses hidden word
    public static void changeWinCondition() {
        winCondition = !winCondition;
    }

    private static boolean loseCondition = false; // Is set to true if user fails to guess the word

    // Collects user String input
    public static String userInput() {
        Scanner keyboard = new Scanner(System.in);
        return keyboard.nextLine();
    }

    // Loads words into validWords and guessable
    public static void loadWords() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("WordleWords"));
            String line;
            while ((line = br.readLine()) != null) {
                validWords.add(line.toUpperCase()); // Loads words into validWords from WordleWords
            }

            BufferedReader br2 = new BufferedReader(new FileReader("WordleGuessable"));
            String line2;
            while ((line2 = br2.readLine()) != null) {
                guessable.add(line2.toUpperCase()); // Loads words into guessable from WordleGuessable
            }

        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    // Chooses a random word from the text file
    public static void chooseWord() {
        if (Driver.demo)
            word = "EERIE";
        else {
            ArrayList<String> wordArr = new ArrayList<>(); // Loads the words from the HashSet to an
                                                           // ArrayList (to access a specifc element)
            for (String word : validWords) {
                wordArr.add(word);
            }
            int rand = (int) (Math.random() * validWords.size()); // Generates a random index
            word = wordArr.get(rand).toUpperCase();
        }
    }

    // Obtains a valid guess from the user and returns it, or returns to menu
    public static String validGuess() {
        String guess = userInput().toUpperCase();
        while (true) {
            if (guess.toUpperCase().equals("MENU"))
                return "MENU";
            else if (guessable.contains(guess))
                return guess.toUpperCase();
            else {
            System.out.println(Driver.RED + Driver.BLACKBG + "Invalid guess.");
            System.out.println(Driver.RESET + Driver.BLACKBG + "Enter a 5-letter word.");
            System.out.print(Driver.RESET + "(" + round + ") ");
            guess = userInput().toUpperCase();
            }
        }
    }

    // Displays feedback upon user guessing the hidden word
    public static void winCondition() {
        streak++;
        System.out.println(Driver.RESET + Driver.GRAY + "-----------------------------------------------------------------------------------------------------------" + Driver.RESET);
        if (streak > longestStreak)
            longestStreak++;
        switch (round) {
            case 1:
                System.out.println("Genius!");
                break;
            case 2:
                System.out.println("Magnificent!");
                break;
            case 3:
                System.out.println("Impressive!");
                break;
            case 4:
                System.out.println("Splendid!");
                break;
            case 5:
                System.out.println("Great!");
                break;
            case 6:
                System.out.println("Whew!");
                break;
        }
        System.out.println("It took you " + round + ((round > 1) ? " guesses." : " guess."));
        System.out.println("Current streak: " + streak);

        System.out.println("Play again? Enter 'yes' or 'menu'");
        String response = userInput();

        while ((!response.toUpperCase().equals("YES")) && (!response.toUpperCase().equals("MENU"))) {
            System.out.println("Invalid input.");
            System.out.println("Play again? Enter 'yes' or 'menu'");
            response = userInput();
        }
        if (response.toUpperCase().equals("YES"))
            playAgain = true;
    }

    // Displays feedback upon user failing to guess the hidden word after 6 tries
    public static void loseCondition() {
        streak = 0;
        System.out.println(Driver.BLACKBG + "The word was " + Driver.BOLD +  word + ".");
        System.out.println(Driver.RESET + Driver.BLACKBG + "Current streak = 0");

        System.out.println("Play again? Enter 'yes' or 'menu'");
        String response = userInput();
        while ((!response.toUpperCase().equals("YES")) && (!response.toUpperCase().equals("MENU"))) {
            System.out.println("Invalid input.");
            System.out.println("Play again? Enter 'yes' or 'menu'");
            response = userInput();
        }
        if (response.toUpperCase().equals("YES"))
            playAgain = true;
    }

    // Starts the game by executing loadWords and chooseWord and calling WordleGuessChecker to review
    public static void start() {
        winCondition = false;
        loseCondition = false;
        playAgain = false;
        round = 0;
        loadWords();
        chooseWord();

        Driver.clearScreen();
        System.out.println(Driver.GREEN + Driver.BOLD + Driver.BLACKBG + "WORDLE");
        System.out.println(Driver.RESET + Driver.BLACKBG + "Enter a 5-letter word.");

        // Runs the game by calling WordleGuessChecker to check user guesses, until win condition or lose condition is reached
        while ((!winCondition) && (!loseCondition)) {
            round++;
            if (round == 7) {
                loseCondition = true;
                loseCondition();
                return;
            }

            System.out.print(Driver.RESET + "(" + round + ") ");
            // Calls validGuess to get a valid guess from the user
            String guess = validGuess();
            if (guess.equals("MENU")) {
                playAgain = false;
                return;
            }
            String feedback = WordleGuessChecker.feedback(guess); // Passes in guess to WordleGuessChecker for feedback
            System.out.println(Driver.RESET + feedback);

            if (winCondition) {
                winCondition();
                return;
            }
        }
    }
}
