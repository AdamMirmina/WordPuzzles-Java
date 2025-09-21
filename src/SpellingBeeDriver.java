import java.util.*;
import java.io.*;

// Driver for Spelling Bee game; chooses a set from the text file, presents it to the player,
// keeps track of player's points/score
public class SpellingBeeDriver
{

    private static int latestScore = 0; // Player's latest score
    public static int getLatestScore() {
        return latestScore;
    }

    private static int highScore = 0; // Player's highest score
    public static int getHighScore() {
        return highScore;
    }

    public static boolean gameOver; // Signifies that the game has ended
    public static boolean playAgain; // Signifies that the user asked to play again

    private static int currentConfig = 0; // Notes the current configuration of letters to shuffle appropriately

    private static ArrayList<String> currentPuzzle = new ArrayList<>(); // ArrayList with information about current puzzle
    private static ArrayList<String> guessedWords = new ArrayList<>(); // Contains the words that the player has successfully made

    // Puzzles that have already been chosen are added here to ensure they are not repeated by accident
    private static ArrayList<Integer> blackList = new ArrayList<>();

    // Collects user String input
    public static String userInput() {
        Scanner keyboard = new Scanner(System.in);
        return keyboard.nextLine();
    }

    // Chooses an appropriate line of the SpellingBee text file and loads it into currentPuzzle
    public static void loadPuzzle() {
        currentPuzzle.clear();
        int line = 0;
        if (Driver.demo) {
            line = 1; // Chooses the first line
            blackList.add(1);
        } else {
            line = (int) (Math.random() * 25) + 1; // Generates a random int 1-25
            if (blackList.size() == 25)
                blackList.clear();
            while (blackList.contains(line))
                line = (int) (Math.random() * 25) + 1;
            blackList.add(line); // Adds appropriate line of the text file to list of lines to not repeat
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("SpellingBee"));
            String[] parts = null;
            for (int i = 1; i <= line; i++) {
                parts = (br.readLine().split(" "));
            }
            for (String part : parts) {
                currentPuzzle.add(part.toUpperCase());
            }

        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    public static void displayPuzzle() {
        if (currentConfig == 9)
            currentConfig = 0;
        System.out.println("     " + Driver.GRAYBG + Driver.BLACK + " " + currentPuzzle.get(currentConfig).charAt(1) + " " + Driver.RESET);
        System.out.println(Driver.BLACK + Driver.GRAYBG + " " + currentPuzzle.get(currentConfig).charAt(6) + " " + Driver.RESET +
            "       " + Driver.GRAYBG + Driver.BLACK + " " + currentPuzzle.get(currentConfig).charAt(2) + " " + Driver.RESET);
        System.out.println("     " + Driver.YELLOWBG + Driver.BLACK + " " + currentPuzzle.get(currentConfig).charAt(0) + " " + Driver.RESET);
        System.out.println(Driver.GRAYBG + Driver.BLACK + " " + currentPuzzle.get(currentConfig).charAt(5) + " " + Driver.RESET +
                "       " + Driver.GRAYBG + Driver.BLACK + " " + currentPuzzle.get(currentConfig).charAt(3) + " " + Driver.RESET);
        System.out.println("     " + Driver.GRAYBG + Driver.BLACK + " " + currentPuzzle.get(currentConfig).charAt(4) + " " + Driver.RESET);
        }

    // Displays feedback after 2 minutes are up
    public static void gameOver() {
        TimerThread.stop();

        System.out.println(Driver.GRAY + "-----------------------------------------------------------------------------------------------------------");
        System.out.println(Driver.RESET + "Time's up!");
        System.out.println("Your score: " + Driver.BOLD + Driver.YELLOW + latestScore);

        if (latestScore > highScore) { // Updates highScore if applicable
            highScore = latestScore;
            System.out.println(Driver.BOLD + Driver.YELLOW + "New high score!");
        }

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

    // Handles the condition where user guesses all possible words in under 2 minutes
    public static void allWordsGuessed() {
        TimerThread.stop();
        System.out.println(Driver.RESET + Driver.BOLD + Driver.YELLOW +
                "Incredible! + Driver.BLACK + You made all possible words!");
        System.out.println("Your score: " + Driver.BOLD + Driver.YELLOW + latestScore);

        if (latestScore > highScore) { // Updates highScore if applicable
            highScore = latestScore;
            System.out.println(Driver.BOLD + Driver.YELLOW + "New high score!");
        }

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

    // Starts the game and runs the game by appropriately handling user input and timer until 2 minutes expire
    public static void start() {
        gameOver = false;
        playAgain = false;
        currentConfig = 0;
        latestScore = 0;
        guessedWords.clear();
        loadPuzzle();
        TimerThread.reset();
        TimerThread.run();

        Driver.clearScreen(4);
        System.out.println(Driver.BLACKBG);
        System.out.println(Driver.YELLOW + Driver.BOLD + Driver.BLACKBG + "  SPELLING BEE");
        System.out.println(Driver.RESET + Driver.BLACKBG + "  Make as many 4+ letter words as possible using these letters.");
        System.out.println("  You must use the center letter at least once.");
        System.out.println("  Enter 's' to shuffle.");
        System.out.println("  You have 2 minutes, starting now!");
        System.out.println(Driver.BLACKBG);
        Driver.clearScreen(1);
        displayPuzzle();
        Driver.clearScreen(4);
        System.out.print(Driver.BLACKBG + "Enter a word:" + Driver.RESET + " ");

        // Runs the game by appropriately handling user input until win condition is reached
        while (!gameOver) {
            String input = userInput();
            if (input.toLowerCase().equals("menu")) { // "Exit to menu" condition
                playAgain = false;
                if (latestScore > highScore) // Updates highScore if applicable
                    highScore = latestScore;
                return;
            }
            if (TimerThread.getElapsedTime() >= 120) { // Game over condition
                gameOver();
                return;
            }

            int index = 0; // Index in currentPuzzle that contains the word the user guessed
            if (currentPuzzle.contains(input.toUpperCase()) && !guessedWords.contains(input.toUpperCase())) {
                index = currentPuzzle.indexOf(input.toUpperCase());
                if ((index < 10) || !(input.matches("[a-zA-Z]+"))) { // Invalid word condition
                    System.out.println(Driver.RESET + Driver.RED + input);
                    System.out.println(Driver.RESET + "You have " + Driver.formatTime2(120 - TimerThread.getElapsedTime()) + " remaining.");
                    Driver.clearScreen(2);
                    displayPuzzle();
                    Driver.clearScreen(2);
                    System.out.print(Driver.RESET + Driver.BLACKBG + "Enter a word:" + Driver.RESET + " ");
                } else { // Valid word condition
                    latestScore += Integer.parseInt(currentPuzzle.get(index + 1));
                    System.out.println(Driver.YELLOW + Driver.BOLD + input.toUpperCase() + Driver.GREEN +
                            "         +" + (currentPuzzle.get(index + 1) + Driver.RESET));
                    guessedWords.add(input.toUpperCase());
                    System.out.println(Driver.YELLOW + Driver.BLACKBG + guessedWords);
                    System.out.println(Driver.RESET + Driver.BLACKBG + "Total score = " + latestScore);
                    System.out.println(Driver.RESET + "You have" +
                            Driver.formatTime2(120 - TimerThread.getElapsedTime()) + " remaining.");
                    Driver.clearScreen(2);
                    displayPuzzle();
                    Driver.clearScreen(2);
                    System.out.print(Driver.RESET + Driver.BLACKBG + "Enter a word:" + Driver.RESET + " ");
                }
            } else if (input.toUpperCase().equals("S")) { // Shuffle condition
                Driver.clearScreen(2);
                currentConfig++;
                displayPuzzle();
                Driver.clearScreen(2);
                System.out.print(Driver.RESET + Driver.BLACKBG + "Enter a word:" + Driver.RESET + " ");
            } else { // Invalid word condition
                System.out.println(Driver.RESET + Driver.RED + input);
                System.out.println(Driver.RESET + "You have " + Driver.formatTime2(120 - TimerThread.getElapsedTime()) + " remaining.");
                Driver.clearScreen(2);
                displayPuzzle();
                Driver.clearScreen(2);
                System.out.print(Driver.RESET + Driver.BLACKBG + "Enter a word:" + Driver.RESET + " ");
            }

            if (currentPuzzle.size() - 10 == guessedWords.size()) { // All words guessed condition
                allWordsGuessed();
                return;
            }
        }
    }
}

