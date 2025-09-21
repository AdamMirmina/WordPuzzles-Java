import java.util.Scanner;

// Controls the game
public class Driver
{
    // Activates demonstration mode for presentation
    public static boolean demo = false;

    // NEW CONCEPT: CHANGING COLOR OF TEXT / BOLD / BACKGROUND COLOR USING ANSI ESCAPE CODES
    public static final String RESET = "\u001B[0m"; // Default text color
    public static final String GREEN = "\u001B[38;5;34m"; // Green text color
    public static final String YELLOW = "\u001B[38;5;178m"; // Yellow text color
    public static final String RED = "\u001B[31m"; // Red text color
    public static final String BLUE = "\u001B[34m"; // Blue text color
    public static final String BLACK = "\u001B[30m"; // Black text color
    public static final String ORANGE = "\u001B[38;5;166m"; // Orange text color
    public static final String PURPLE = "\u001B[38;5;135m"; // Purple text color
    public static final String GRAY = "\u001B[37m"; // Gray text color
    public static final String CYAN = "\u001B[38;5;44m"; // Cyan text color

    public static final String BOLD = "\u001B[1m"; // Bold text

    public static final String BLACKBG = "\u001B[40m"; // Black background
    public static final String GREENBG = "\u001B[42m"; // Green background
    public static final String YELLOWBG = "\u001B[43m"; // Yellow background
    public static final String GRAYBG =  "\u001B[47m"; // Light gray background

    // Clears the screen by adding blank lines
    public static void clearScreen(int lines) {
        for (int i = 0; i < lines; i++)
            System.out.println(Driver.RESET);
    }

    public static void clearScreen() {
        for (int i = 0; i < 17; i++)
            System.out.println(Driver.RESET);
    }

    // Formats time in mm:ss
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secondsRemaining = seconds % 60;
        String formatted = String.format("%02d:%02d", minutes, secondsRemaining); // %02d: leading 0, 2 digits, decimal integer
        return formatted;
    }

    // Formats time in m:ss
    public static String formatTime2(int seconds) {
        int minutes = seconds / 60;
        int secondsRemaining = seconds % 60;
        String formatted = String.format("%2d:%02d", minutes, secondsRemaining); // %02d: leading 0, 2 digits, decimal integer
        return formatted;
    }

    // Presents the available word games and lets the user choose which to play
    public static void menu() {
        Scanner keyboard = new Scanner(System.in);

        // This loop will repeat indefinitely as the menu screen and a series of return calls will reactivate it
        while (true) {
            System.out.println(BLACKBG);
            System.out.println(BLACKBG + PURPLE + BOLD + "  WORD PUZZLES");
            System.out.println(RESET + BLACKBG + CYAN + "  By Adam Mirmina");
            System.out.println(BLACKBG);
            System.out.println(RESET + BLACKBG + GRAY + "-----------------------------------------------------------------------------------------------------------");
            System.out.println(RESET + BLACKBG + "  To play " + GREEN + BOLD + "WORDLE: " + RESET + BLACKBG + "Enter 1" +
                    "\n       (Current streak: " + GREEN + BOLD + WordleDriver.getStreak() + RESET + BLACKBG + ")" +
                    " (Longest streak: " + GREEN + BOLD + WordleDriver.getLongestStreak() + RESET + BLACKBG + ")");
            System.out.println(GRAY + "-----------------------------------------------------------------------------------------------------------");
            System.out.println(RESET + BLACKBG + "  To play " + YELLOW + BOLD + "SPELLING BEE: " + RESET + BLACKBG + "Enter 2" +
                    "\n       (Latest score: " + YELLOW + BOLD + SpellingBeeDriver.getLatestScore() + RESET + BLACKBG + ")" +
                    " (Best score: " + YELLOW + BOLD + SpellingBeeDriver.getHighScore() + RESET + BLACKBG + ")");
            System.out.println(GRAY + "-----------------------------------------------------------------------------------------------------------");
            System.out.println(RESET + BLACKBG + "  To play " + ORANGE + BOLD + "ANAGRAMS: " + RESET + BLACKBG + "Enter 3" +
                    "\n       (Latest time: " + ORANGE + BOLD + AnagramDriver.getLatestTimeDisplay() + RESET + BLACKBG + ")" +
                    " (Best time: " + ORANGE + BOLD + AnagramDriver.getBestTimeDisplay() + RESET + BLACKBG + ")");
            System.out.println(GRAY + "-----------------------------------------------------------------------------------------------------------");
            System.out.println(RESET + BLACKBG + "  Enter 'menu' at any time to return here.");
            System.out.println(BLACKBG);

            clearScreen(1);
            System.out.println("___________________________________________________________________________________________________________");
            System.out.println(BLACKBG + "Enter your selection:" + RESET + " ");

            // Takes in and verifies user's choice of game
            while (true) {
                String input = keyboard.nextLine();
                if (input.toUpperCase().equals("1")) { // Activates Wordle
                    demo = false;
                    WordleDriver.start();
                    while (WordleDriver.playAgain == true) {
                        demo = false;
                        WordleDriver.start();
                    } break;
                } else if (input.toUpperCase().equals("DEMO.1")) { // Activates Wordle demonstration mode
                    demo = true;
                    WordleDriver.start();
                    while (WordleDriver.playAgain == true) {
                        demo = true;
                        WordleDriver.start();
                    }
                    break;
                } else if (input.toUpperCase().equals("2")) { // Activates Spelling Bee
                        demo = false;
                        SpellingBeeDriver.start();
                        while (SpellingBeeDriver.playAgain == true) {
                            demo = false;
                            SpellingBeeDriver.start();
                        } break;
                } else if (input.toUpperCase().equals("DEMO.2")) { // Activates Spelling Bee demonstration mode
                        demo = true;
                        SpellingBeeDriver.start();
                        while (SpellingBeeDriver.playAgain == true) {
                            demo = true;
                            SpellingBeeDriver.start();
                        }
                        break;
                } else if (input.toUpperCase().equals("3")) { // Activates Anagrams
                    demo = false;
                    AnagramDriver.start();
                    while (AnagramDriver.playAgain == true) {
                        demo = false;
                        AnagramDriver.start();
                    } break;
                } else if (input.toUpperCase().equals("DEMO.3")) { // Activates Anagrams demonstration mode
                    demo = true;
                    AnagramDriver.start();
                    while (AnagramDriver.playAgain == true) {
                        demo = true;
                        AnagramDriver.start();
                    } break;
                } else if (input.toUpperCase().equals("MENU")) { // Exits inner loop to return to menu screen
                    break;
                } else
                    System.out.print(BLACKBG + "Enter your selection:" + RESET + " ");
            }
        }
    }

    // Starts the game by calling menu to display menu screen
    public static void main(String[] args) {
        menu();
    }
}
