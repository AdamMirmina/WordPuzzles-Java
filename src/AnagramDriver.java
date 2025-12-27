import java.util.*;
import java.io.*;

// Driver for Anagrams game; chooses an anagram from the text file, presents it to the user,
// determines win/loss status, and keeps track of the user's times
public class AnagramDriver {

    private static int latestTime; // User's latest time
    private static String latestTimeDisplay = ""; // Displays the user's latest time in mm:ss format
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

    private static final int MAX_WORDS = 80;
    private static final int COLS = 5;
    private static String[][] words = new String[MAX_WORDS][COLS]; // 2D array with scrambled and unscrambled words
    private static int loadedCount = 0; // how many valid rows were loaded from the file

    private static boolean winCondition = false; // Is set to true if user unscrambles the word
    public static void changeWinCondition() {
        winCondition = !winCondition;
    }

    private static final ArrayList<Integer> blackList = new ArrayList<>(); // prevent repeats
    private static final Scanner keyboard = new Scanner(System.in);

    // Collects user String input
    public static String userInput() {
        return keyboard.nextLine();
    }

    // Loads words into words[][]; returns true if at least 1 row loaded
    public static boolean loadWords() {
        loadedCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("Anagrams"))) {
            String line;
            while ((line = br.readLine()) != null && loadedCount < MAX_WORDS) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");
                // Expect exactly 5 tokens per line (0..4). Skip malformed lines.
                if (parts.length < COLS) continue;

                words[loadedCount] = parts;
                loadedCount++;
            }
        } catch (IOException e) {
            System.out.println("File not found.");
            return false;
        }

        if (loadedCount == 0) {
            System.out.println("No anagrams loaded (file may be empty or malformed).");
            return false;
        }
        return true;
    }

    // Chooses a random anagram from the loaded words
    public static void chooseWord() {
        // If demo expects a specific row, only use it if it exists in the loaded data.
        if (Driver.demo && loadedCount > 21 && words[21] != null && words[21].length >= COLS) {
            word = words[21][4]; // Defiant
            puzzleRow = 21;
            if (!blackList.contains(21)) blackList.add(21);
            return;
        }

        // Normal mode: choose from rows actually loaded
        if (blackList.size() >= loadedCount) blackList.clear();

        int rand = (int) (Math.random() * loadedCount); // 0..loadedCount-1
        while (blackList.contains(rand)) {
            rand = (int) (Math.random() * loadedCount);
        }

        puzzleRow = rand;
        word = words[puzzleRow][4];
        blackList.add(puzzleRow);
    }

    // Gives a hint to the player and updates hintsAvailable
    public static void giveHint() {
        if (hintsAvailable == 0) {
            System.out.println(Driver.RESET + "No more hints available!");
            return;
        }

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
                break;
        }
        hintsAvailable--;
    }

    // Shuffles the letters and updates shufflesAvailable
    public static void shuffle() {
        if (shufflesAvailable == 0) {
            System.out.println(Driver.RESET + "No more shuffles available!");
            return;
        }

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
                break;
        }
        shufflesAvailable--;
        System.out.println(Driver.RESET + "                    Shuffles remaining: " + shufflesAvailable);
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
            System.out.println(Driver.BOLD + Driver.ORANGE + "New personal best!" + Driver.RESET);

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

        // If file missing / empty, exit this game cleanly instead of crashing later
        if (!loadWords()) return;

        chooseWord();
        TimerThread.reset();
        TimerThread.run();

        System.out.println(Driver.BLACKBG);
        System.out.println(Driver.ORANGE + Driver.BOLD + Driver.BLACKBG + "  ANAGRAMS" + Driver.RESET);
        System.out.println(Driver.RESET + Driver.BLACKBG + "  Unscramble the word.");
        System.out.println("  Enter 's' to shuffle (5 second time penalty).");
        System.out.println("  Enter 'h' for hint (10 second time penalty).");
        System.out.println(Driver.BLACKBG);
        Driver.clearScreen(1);
        System.out.println("  Shuffles remaining: 3");
        System.out.println("  Hints remaining: 3");
        Driver.clearScreen(9);
        System.out.println(Driver.BLACKBG + "Your word:" + Driver.RESET + " "
                + Driver.ORANGE + Driver.BOLD + words[puzzleRow][0] + Driver.RESET);

        // Runs the game by appropriately handling user input until win condition is reached
        while (!winCondition) {
            String guess = userInput();

            if (guess.toLowerCase().equals("menu")) {
                playAgain = false;
                return;
            } else if (guess.toLowerCase().equals(word)) {
                winCondition();
                return;
            } else if (guess.toLowerCase().equals("h")) {
                giveHint();
            } else if (guess.toLowerCase().equals("s")) {
                shuffle();
            } else {
                // IMPORTANT: reset after printing colored text so color doesn't "leak" to later lines
                System.out.println(Driver.RED + "Incorrect word." + Driver.RESET);
            }
        }
    }
}
