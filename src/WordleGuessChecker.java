import java.util.ArrayList;

// Returns feedback on the user's guess (green: correct letter and spot, yellow: correct letter,
// wrong spot, gray: not in word)
public class WordleGuessChecker
{
    // Is added to letter by letter when appropriate feedback for each letter of the guess is determined
    private static String feedback = "";

    // LOGIC: All characters in this method are in the word but in the wrong place.
    // (1) If letter appears the same or a greater number of times in word than it does in guess, feedback is yellow.
    // (2) If the letter appears fewer times in word than it does in guess, it must first be determined if
    //    (a) guess contains 2-3 occurences of the letter and word contains 1, or
    //    (b) guess contains 3 of the letter and word contains 2
    //    (no valid 5-letter English word contains 4 of the same letter).
    // (3) Check feedback for duplicate letters that come before or after;
    //    - Case a: if feedback contains any one past instance of the letter (yellow or green),
    //          OR there is a future green instance,
    //          feedback is gray. If not, feedback is yellow.
    //    - Case b: if feedback contains 2 past instances of the letter, either as a yellow or green,
    //          OR there are 2 future green instances of the letter,
    //          OR there is one green past and one green future occurence,
    //          feedback is gray. If not, feedback is yellow.
    public static String yellowDeterminer(String word, String guess, char letter, int index) {
        int numAppearancesWord = numAppearances(word, letter);
        int numAppearancesGuess = numAppearances(guess, letter);
        if (numAppearancesWord >= numAppearancesGuess)
            return Driver.BLACK + Driver.YELLOWBG + " " + letter + " " + Driver.RESET + " "; // (1)

        else if (numAppearancesWord == 1) {
            return ((pastChecker(guess, letter, index) >= 1) || (futureCheckerGreen(guess, letter, index) >= 1)) ?
                Driver.BLACK + Driver.GRAYBG + " " + letter + " " + Driver.RESET + " " : // Case a.1
                Driver.BLACK + Driver.YELLOWBG + " " + letter + " " + Driver.RESET + " "; // Case a.2

        } else
            return ((numAppearances(guess.substring(0, index), letter) == 2) ||
                    (futureCheckerGreen(guess, letter, index) == 2) ||
                    (((futureCheckerGreen(guess, letter, index) == 1) && (pastCheckerGreen(guess, letter, index) == 1)))) ?
                        Driver.BLACK + Driver.GRAYBG + " " + letter + " " + Driver.RESET + " " : // Case b.1
                        Driver.BLACK + Driver.YELLOWBG + " " + letter + " " + Driver.RESET + " "; // Case b.2
    }

    // Returns the number of appearances of a given letter in hidden word
    public static int numAppearances(String word, char letter) {
        int num = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter)
                num++;
        }
        return num;
    }

    // Checks the rest of the word that came before the character in question for green duplicate letters
    public static int pastCheckerGreen(String guess, char letter, int index) {
        int greenDups = 0;
        for (int i = 0; i < index; i++) {
            if ((guess.charAt(i) == WordleDriver.getWord().charAt(i)) && (guess.charAt(i) == letter))
                greenDups++;
        }
        return greenDups;
    }

    // Checks the rest of the word that came after the character in question for green duplicate letters
    public static int futureCheckerGreen(String guess, char letter, int index) {
        int greenDups = 0;
        for (int i = index; i < 5; i++) {
            if ((guess.charAt(i) == WordleDriver.getWord().charAt(i)) && (guess.charAt(i) == letter))
                greenDups++;
        }
        return greenDups;
    }

    // Checks the rest of the word that came before the character in question for duplicate letters
    public static int pastChecker(String guess, char letter, int index) {
        int dups = 0;
        for (int i = 0; i < index; i++) {
            if (guess.charAt(i) == letter)
                dups++;
        }
        return dups;
    }

    // Returns feedback
    public static String feedback(String guess) {
        String word = WordleDriver.getWord();
        int numGreenLetters = 0; // Counts the number of green letters in word feedback
        feedback = "";

        // LOGIC: if a letter is correct and in the right spot, return green. If it's not in the word,
        // return yellow. Else, send to yellowDeterminer to sort out proper feedback.
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == word.charAt(i)) { // Test for green feedback
                feedback += Driver.GREENBG + Driver.BLACK + " " + guess.charAt(i) + " " + Driver.RESET + " ";
                numGreenLetters++;
                if (numGreenLetters == 5)
                    WordleDriver.changeWinCondition(); // Signals to WordleDriver that win condition has been reached
            } else if (!word.contains(guess.substring(i, i + 1))) // Test for gray feedback
                feedback += Driver.GRAYBG + Driver.BLACK + " " + guess.charAt(i) + " " + Driver.RESET + " ";
            else // If in word but wrong place, sends character to yellowDeterminer to determine status
                feedback += yellowDeterminer(word, guess, guess.charAt(i), i);

        }
        return feedback;
    }
}
