package uk.ac.standrews.cs5031;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The only executable. Contains the main game loop.
 *
 * @author Edwin Brady, 150009974
 * @version 4.1
 */
public final class Hangman {

    /** The {@link Scanner} used to read from standard input. */
    private static Scanner reader;

    /** Instantiates the object used to read the standard input. */
    private static void instantiateReader() {
        reader = new Scanner(System.in, "utf-8");
        reader.useDelimiter("\n");
    }

    /**
     * Returns true if and only if the given line represents a "yes".
     * It is checked against "yes", "yep", "y", and "true".
     * This operation is case-sensitive.
     *
     * @param line the line to check
     * @return true iff the line is a yes
     */
    private static boolean isYes(final String line) {
        return line.equals("yes")
                || line.equals("yep")
                || line.equals("y")
                || line.equals("true");
    }

    /**
     * Returns true if and only if the given line represents a "yes".
     * It is checked against "no", "nope", "n", and "false".
     * This operation is case-sensitive.
     *
     * @param line the line to check
     * @return true iff the line is a no
     */
    private static boolean isNo(final String line) {
        return line.equals("no")
                || line.equals("nope")
                || line.equals("n")
                || line.equals("false");
    }

    /**
     * Returns true if and only if the player wants to play another game.
     * The question is outputted to standard output and
     * the player's answer is read from standard input.
     * If parsing of the argument fails, the player is asked again.
     * Once an answer is successfully parsed, the method returns
     * true if the player answer yes
     * false if the player answered no.
     *
     * @return true iff the player answers yes, false iff the player answers no
     * @see Hangman#isYes(String)
     * @see Hangman#isNo(String)
     */
    private static boolean doesPlayerWantAnotherGame() {
        System.out.println("Would you like to play again?");
        do {
            String answer = reader.next().toLowerCase();
            if (isYes(answer)) {
                return true;
            }
            if (isNo(answer)) {
                return false;
            }
            System.out.println("Failed to parse answer, please type again.");
        } while (true);
    }

    /**
     * Plays games until the player quits.
     * Specifically, in a loop, calls
     * {@link Game#play(Scanner)},
     * {@link Game#printGameOverMessage()}, and
     * {@link Hangman#doesPlayerWantAnotherGame()}
     * until the player responds with no, or the program reaches end of input.
     *
     * @throws NoSuchElementException on end of input
     */
    private static void playGamesUntilQuit() throws NoSuchElementException {
        boolean playAgain;
        do {
            Game.play(reader);
            Game.printGameOverMessage();
            playAgain = doesPlayerWantAnotherGame();
        } while (playAgain);
    }

    /**
     * The runnable method.
     * This method makes the necessary calls to other methods, so as to
     * parse the arguments as game options,
     * load any custom phrases,
     * play games.
     *
     * @param args the command line arguments to be parse as game options
     * @see OptionsParser#parseAndValidate(String[])
     * @see Phrases#loadCustom()
     * @see Hangman#playGamesUntilQuit()
     */
    public static void main(final String[] args) {
        try {
            OptionsParser.parseAndValidate(args);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return;
        }
        instantiateReader();
        try {
            Phrases.loadCustom();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            playGamesUntilQuit();
        } catch (NoSuchElementException ignored) {
            // This exception is thrown only upon end of input,
            // in which case there is nothing to be done.
        }
    }

    /** Hides the constructor for this utility class. */
    private Hangman() {
    }

}
