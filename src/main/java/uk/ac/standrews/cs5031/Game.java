package uk.ac.standrews.cs5031;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.InputMismatchException;

import static uk.ac.standrews.cs5031.Phrases.CITIES_ID;
import static uk.ac.standrews.cs5031.Phrases.COUNTIES_ID;
import static uk.ac.standrews.cs5031.Phrases.COUNTRIES_ID;
import static uk.ac.standrews.cs5031.Phrases.CUSTOM_ID;

/**
 * An instance of this class represents the current state of the game.
 *
 * @author Edwin Brady, 150009974
 * @version 3.7
 */
public final class Game {

    /** The error message displayed when an invalid category is typed. */
    private static final String CATEGORY_NOT_RECOGNIZED =
            "Category not recognized.";

    /** The {@link Scanner} used to read from standard input. */
    private static Scanner reader;

    /** The phrase to be guessed in the Hangman game. */
    private static String phrase;

    /** The amount of guesses made so far. */
    private static int guessesMade;

    /** The amount of lives remaining. */
    private static int lives;

    /** The amount of hints remaining. */
    private static int hints;

    /** The set of letters that have been inputted. */
    private static LinkedHashSet<Character> guessedLetters;

    /** The set of letters from the phrase that have not yet been guessed. */
    private static LinkedHashSet<Character> remainingLetters;

    /**
     * Sets up and plays through a game of Hangman.
     *
     * @param providedReader the input reader to use
     */
    public static void play(final Scanner providedReader) {
        instantiateReader(providedReader);
        setUp();
        mainLoop();
    }

    /**
     * Sets the {@link Game#reader} to the provided one.
     * This way the methods in this class can read continues input
     * provided from {@link Hangman}.
     *
     * If the given argument is null, re-instantiates the input reader.
     * This way tests can supply different inputs.
     *
     * @param providedReader the input reader to use
     */
    private static void instantiateReader(final Scanner providedReader) {
        if (providedReader != null) {
            reader = providedReader;
        } else {
            reader = new Scanner(System.in, "utf-8");
            reader.useDelimiter("\n");
        }
    }

    /**
     * Prints a Game over message to the screen.
     * Congratulates if the game was won.
     * Reveals the phrase if the game was lost.
     */
    public static void printGameOverMessage() {
        if (hasWon()) {
            System.out.println("You won!");
            System.out.println("You took " + guessesMade + " guesses.");
            System.out.println("Phrase: " + phrase);
        } else if (hasLost()) {
            System.out.println("You lost!");
            System.out.println("Phrase: " + phrase);
        }
    }

    /**
     * Initialises the variables for this game.
     * Resets guesses, lives, hints, and chooses a new phrase.
     *
     * @see Game#determineTarget()
     * @see Game#reader
     */
    private static void setUp() {
        guessesMade = 0;
        lives = OptionsParser.getLives();
        hints = OptionsParser.getMaxHints();
        phrase = determineTarget();
        guessedLetters = new LinkedHashSet<>();
        remainingLetters = new LinkedHashSet<>();

        for (int i = 0; i < phrase.length(); ++i) {
            char c = Character.toLowerCase(phrase.charAt(i));
            remainingLetters.add(c);
        }

        // No need to guess spaces.
        remainingLetters.remove(' ');
        guessedLetters.add(' ');
    }

    /**
     * Determines the target phrase for the Hangman game.
     * Asks the player to enter a number corresponding to a category ID.
     * Then calls {@link Phrases#getRandomPhrase(int)} to retrieve a
     * pseudo-random phrase from that category.
     *
     * @return a pseudo-random phrase from a player-selected category
     */
    private static String determineTarget() {
        if (OptionsParser.areThereCustomPhrases()) {
            System.out.println(formatCategory(CUSTOM_ID, "Custom"));
        }
        System.out.println(formatCategory(COUNTRIES_ID, "Countries"));
        System.out.println(formatCategory(COUNTIES_ID, "Counties"));
        System.out.println(formatCategory(CITIES_ID, "Cities"));
        System.out.print("Pick a category [");
        if (OptionsParser.areThereCustomPhrases()) {
            System.out.print("0, ");
        }
        System.out.println("1, 2, or 3]:");
        do {
            try {
                int category = reader.nextInt();
                return Phrases.getRandomPhrase(category);
            } catch (IllegalArgumentException e) {
                System.out.println(CATEGORY_NOT_RECOGNIZED);
            } catch (InputMismatchException e) {
                reader.next();
                System.out.println(CATEGORY_NOT_RECOGNIZED);
            }
        } while (true);
    }

    /**
     * Creates and returns a formatted String to be printed on the console.
     * The parameters should be id of category from {@link Phrases}
     * and respective name.
     *
     * @param id   the id of the category
     * @param name the name of the category
     * @return the formatted string
     */
    private static String formatCategory(final int id, final String name) {
        return "  " + id + ". " + name;
    }

    /**
     * In a loop:
     * prints the phrase to the standard output and waits for a guess.
     * The loop ends when the game is either won or lost.
     */
    private static void mainLoop() {
        while (!hasWon() && !hasLost()) {
            printPhrase();
            printState();
            String input = reader.next();

            if (input.equals("?")) {
                printHint();
                continue;
            }
            checkGuess(input);
        }
    }

    /**
     * Tells whether the game has been won.
     * Returns true if and only if there are no remaining letters to be guessed.
     *
     * @return true iff all letters have been guessed
     */
    private static boolean hasWon() {
        return remainingLetters.isEmpty();
    }

    /**
     * Tells whether the game has been lost.
     * Returns true if and only if there are no lives left.
     *
     * @return true iff there are no lives left
     */
    private static boolean hasLost() {
        return lives == 0;
    }

    /**
     * Outputs the current phrase to the standard output.
     * Letters that have not yet been guessed are replaced with dashes.
     */
    private static void printPhrase() {
        for (int i = 0; i < phrase.length(); ++i) {
            char original = phrase.charAt(i);
            char lower = Character.toLowerCase(original);
            if (guessedLetters.contains(lower)) {
                System.out.print(original);
            } else {
                System.out.print("-");
            }
        }
        // Output a new line after the phrase.
        System.out.println();
    }

    /** Outputs the current state of the game to standard output. */
    private static void printState() {
        System.out.println(remainingLetters.size() + " letters remaining.");
        System.out.println("Lives remaining: " + lives);
        String hintsLeft = "(? for a hint [" + hints + " remaining]):";
        System.out.println("Guess a letter or phrase " + hintsLeft);
    }

    /**
     * Checks the guess made by the player against the phrase.
     * If the guess contains exactly one letter, the presence of the letter
     * in the phrase is checked.
     * If the guess is made of multiple letters, it is compared to the whole
     * phrase.
     *
     * @param guess the guess made by the player
     */
    private static void checkGuess(final String guess) {
        if (guess.length() > 1) {
            checkFullPhrase(guess);
        } else if (!guess.isEmpty()) {
            checkLetter(guess.charAt(0));
        }
    }

    /**
     * Checks if the given guess correctly matches the phrase.
     * This check is case-insensitive.
     *
     * @param guess the guess made by the player
     */
    private static void checkFullPhrase(final String guess) {
        guessesMade++;
        String lowerCaseGuess = guess.toLowerCase();
        String lowerCasePhrase = phrase.toLowerCase();
        if (lowerCaseGuess.equals(lowerCasePhrase)) {
            guessedLetters.addAll(remainingLetters);
            remainingLetters.clear();
        } else {
            System.out.println("Wrong!");
            lives--;
        }
    }

    /**
     * Checks if the guessed letter is in the phrase.
     * This check is case-insensitive.
     *
     * @param letter the guess made by the player
     */
    private static void checkLetter(final char letter) {
        char lower = Character.toLowerCase(letter);
        // Repeat guess
        if (guessedLetters.contains(lower)) {
            return;
        }

        guessesMade++;
        guessedLetters.add(lower);

        // Correct guess
        if (remainingLetters.contains(lower)) {
            System.out.println("Correct!");
            remainingLetters.remove(lower);
        } else {
            System.out.println("Wrong!");
            lives--;
        }
    }

    /** Outputs a hint to the standard output if such are still allowed. */
    private static void printHint() {
        if (hints == 0) {
            System.out.println("No more hints allowed!");
            return;
        }

        System.out.print("Try: ");
        Character[] letters = new Character[remainingLetters.size()];
        letters = remainingLetters.toArray(letters);
        int index = Phrases.GENERATOR.nextInt(letters.length);
        System.out.println(letters[index]);
        hints--;
    }

    /** Hides the constructor for this utility class. */
    private Game() {
    }

}
