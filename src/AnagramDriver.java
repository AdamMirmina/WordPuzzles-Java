import java.util.*;
import java.io.*;

// Driver for Anagrams game; chooses an anagram from the text file, presents it to the user,
// determines win/loss status, and keeps track of the user's times
public class AnagramDriver
{

    private static int latestTime; // User's latest time
    private static String latestTimeDisplay = ""; // // Displays the user's latest time in mm:ss format
    public static String getLatestTimeDisplay() {
        return (latestTimeDisplay.equals("") ? "00:00" : latestTimeDisplay);
    }

    private static int bestTime = 0; // User's best time
    private static String bestTimeDisplay = ""; // Displays the user's best time in mm:ss format

    public static String getBestTimeDisplay() {
        return (bestTimeDisplay.equals("") ? "00:00" : bestTimeDisplay);
    }

    public static boolean playAgain; // Signifies that the user asked to play again

    private static String word; // Word that the user tries to unscramble
    private static int puzzleRow; // Row in the words[][] array of the scrambled and unscrambled words in the current round

    private static int shufflesAvailable = 0;
    private static int hintsAvailable = 0;

    private static String[][] words = new String[80][5]; // 2D array with scrambled and unscrambled words

    private static boolean winCondition = false; // Is set to true if user unscrambles the word
    public static void changeWinCondition() {
        winCondition = !winCondition;
    }

    private static ArrayList<Integer> blackList = new ArrayList<>(); // Indices of anagrams that have already been chosen
                                                             // are added here to ensure they are not repeated by accident

    // Collects user String input
    public static String userInput() {
        Scanner keyboard = new Scanner(System.in);
        return keyboard.nextLine();
    }

    // Loads words into words
    public static void loadWords() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Anagrams"));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                words[i] = line.split(" ");
                i++;
            }

        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    // Chooses a random anagram from the text file
    public static void chooseWord() {
        if (Driver.demo) {
            word = words[21][4]; // Defiant
            puzzleRow = 21;
            blackList.add(21);
        } else {
            int rand = (int) (Math.random() * 80); // Generates a random index 0-80
            if (blackList.size() == 80)
                blackList.clear();
            while (blackList.contains(rand))
                rand = (int) (Math.random() * 80);
            word = words[rand][4];
            puzzleRow = rand;
            blackList.add(puzzleRow);
        }
    }

    // Gives a hint to the player and updates hintsAvailable
    public static void giveHint() {
        if (hintsAvailable == 0)
            System.out.println(Driver.RESET + "No more hints available!");
        // Prints either the first, first and second, or first / second / third characters of the correct word
        else {
            System.out.println(Driver.BOLD + Driver.ORANGE);
            switch (hintsAvailable) {
                case 3:
                    System.out.print(Driver.BOLD + Driver.ORANGE + words[puzzleRow][4].substring(0, 1));
                    System.out.println(Driver.RESET + "                          Hints remaining: 2");
                    break;
                case 2:
                    System.out.print(Driver.BOLD + Driver.ORANGE + words[puzzleRow][4].substring(0, 2));
                    System.out.println(Driver.RESET + "                         Hints remaining: 1");
                    break;
                case 1:
                    System.out.print(Driver.BOLD + Driver.ORANGE + words[puzzleRow][4].substring(0, 3));
                    System.out.println(Driver.RESET + "                        Hints remaining: 0");
                }
            hintsAvailable--;
            }
        }

    // Shuffles the letters and updates shufflesAvailable
    public static void shuffle() {
        if (shufflesAvailable == 0)
            System.out.println(Driver.RESET + "No more shuffles available!");

        // Prints the applicable anagram from words array
        else {
            System.out.print(Driver.BOLD + Driver.ORANGE);
            switch (shufflesAvailable) {
                case 3:
                    System.out.print(words[puzzleRow][1]);
                    break;
                case 2:
                    System.out.print(words[puzzleRow][2]);
                    break;
                case 1:
                    System.out.print(words[puzzleRow][3]);
            }
            shufflesAvailable--;
            System.out.println(Driver.RESET + "                    Shuffles remaining: " + shufflesAvailable);
        }
    }

    // Displays feedback upon user successfully unscrambling the word
    public static void winCondition() {
        TimerThread.stop();
        latestTime = TimerThread.getElapsedTime();
        latestTimeDisplay = Driver.formatTime(latestTime);

        System.out.println(Driver.GRAY + "-----------------------------------------------------------------------------------------------------------");
        System.out.println(Driver.RESET + "You got it!");
        System.out.println("It took you " + ((latestTime < 60) ? latestTime + " seconds" : latestTimeDisplay) + ".");

        if ((hintsAvailable < 3) || (shufflesAvailable < 3)) { // Assigns time penalties if applicable
            if (hintsAvailable == 2)
                latestTime += 10;
            else if (hintsAvailable == 1)
                latestTime += 20;
            else if (hintsAvailable == 0)
                latestTime += 30;

            if (shufflesAvailable == 2)
                latestTime += 5;
            else if (shufflesAvailable == 1)
                latestTime += 10;
            else if (shufflesAvailable == 0)
                latestTime += 15;
            latestTimeDisplay = Driver.formatTime(latestTime);
            System.out.println("Final time including penalties: " + Driver.BOLD + Driver.ORANGE + latestTimeDisplay + Driver.RESET);
        }

        if (bestTime == 0) { // Updates best time if applicable
            bestTime = latestTime;
            bestTimeDisplay = latestTimeDisplay;
        } else if (latestTime < bestTime) {
            bestTime = latestTime;
            bestTimeDisplay = latestTimeDisplay;
        }

        if (latestTime == bestTime)
            System.out.println(Driver.BOLD + Driver.ORANGE + "New personal best!");

        System.out.println(Driver.RESET + "Play again? Enter 'yes' or 'menu'");
        String response = userInput();

        while ((!response.toLowerCase().equals("yes")) && (!response.toLowerCase().equals("menu"))) {
            System.out.println("Invalid input.");
            System.out.println("Play again? Enter 'yes' or 'menu'");
            response = userInput();
        }
        if (response.toLowerCase().equals("yes"))
            playAgain = true;
    }

    // Starts the game and runs the game by appropriately handling user input and timer until win condition is reached
    public static void start() {
        winCondition = false;
        playAgain = false;
        hintsAvailable = 3;
        shufflesAvailable = 3;
        loadWords();
        chooseWord();
        TimerThread.reset();
        TimerThread.run();

        System.out.println(Driver.BLACKBG);
        System.out.println(Driver.ORANGE + Driver.BOLD + Driver.BLACKBG + "  ANAGRAMS");
        System.out.println(Driver.RESET + Driver.BLACKBG + "  Unscramble the word.");
        System.out.println("  Enter 's' to shuffle (5 second time penalty).");
        System.out.println("  Enter 'h' for hint (10 second time penalty).");
        System.out.println(Driver.BLACKBG);
        Driver.clearScreen(1);
        System.out.println("  Shuffles remaining: 3");
        System.out.println("  Hints remaining: 3");
        Driver.clearScreen(9);
        System.out.println(Driver.BLACKBG + "Your word:" + Driver.RESET + " "
                + Driver.ORANGE + Driver.BOLD + words[puzzleRow][0]);

        // Runs the game by appropriately handling user input until win condition is reached
        while (!winCondition) {
            String guess = userInput();
            if (guess.toLowerCase().equals("menu")) {
                playAgain = false;
                return;
            }
            else if (guess.toLowerCase().equals(word)) {
                winCondition();
                return;
            }
            else if (guess.toLowerCase().equals("h"))
                giveHint();
            else if (guess.toLowerCase().equals("s"))
                shuffle();
            else
                System.out.println(Driver.RESET + Driver.RED + "Incorrect word.");
        }
    }
}
