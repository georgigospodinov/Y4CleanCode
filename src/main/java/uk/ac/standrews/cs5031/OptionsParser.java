package uk.ac.standrews.cs5031;

/**
 * This class provides methods to parse and retrieve
 * the options for a single execution of the system.
 *
 * @author Edwin Brady, 150009974
 * @version 3.3
 */
public final class OptionsParser {

    /**
     * The command line option used to specify the amount of lives
     * the player starts with.
     */
    public static final String LIVES_OPTION = "--lives";

    /**
     * The command line option used to specify the amount of hints
     * the player starts with.
     */
    public static final String MAX_HINTS_OPTION = "--max-hints";

    /** The default amount of lives the player starts with. */
    public static final int DEFAULT_LIVES = 10;

    /** The default amount of hints the player starts with. */
    public static final int DEFAULT_MAX_HINTS = 2;

    /**
     * The no name of file from which to choose phrases.
     * The system should use the built-in options.
     */
    public static final String NO_PHRASE_SOURCE = "";

    /** The message of the exception when the amount of lives is invalid. */
    public static final String INVALID_LIVES =
            "The amount of lives must be a positive number.";

    /** The message of the exception when the amount of hints is invalid. */
    public static final String INVALID_HINTS =
            "The amount of lives must be a non-negative number.";

    /** The amount of lives the player starts with. */
    private static int lives;

    /** The amount of hints the player starts with. */
    private static int maxHints;

    /** The name of the file from which to choose phrases. */
    private static String phraseSource;

    /**
     * Retrieves the starting amount of lives the player has.
     *
     * @return the starting amount of lives
     */
    public static int getLives() {
        return lives;
    }

    /**
     * Retrieves the starting amount of hints the player has.
     *
     * @return the starting amount of hints
     */
    public static int getMaxHints() {
        return maxHints;
    }

    /**
     * Retrieves the name of the file from which to choose phrases.
     *
     * @return the name of the file with source phrases
     */
    public static String getPhraseSource() {
        return phraseSource;
    }

    /**
     * Returns true if the phrase source has been set.
     *
     * @return true iff the phrase source is a non-empty string
     */
    public static boolean areThereCustomPhrases() {
        return !phraseSource.equals(NO_PHRASE_SOURCE);
    }

    /**
     * Parses and validates the options from the command line arguments.
     *
     * @param args the command line arguments provided upon execution
     * @throws NumberFormatException if the amount of lives is not positive
     *                               or if the amount of hints is negative
     */
    public static void parseAndValidate(final String[] args)
            throws NumberFormatException {
        initialise();
        parse(args);
        validateOptions();
    }

    /**
     * Initialises the game options to their default values.
     * Any options that are not set from the command line will use these values.
     */
    private static void initialise() {
        lives = DEFAULT_LIVES;
        maxHints = DEFAULT_MAX_HINTS;
        phraseSource = NO_PHRASE_SOURCE;
    }

    /**
     * Parses the command line arguments provided when the application is run.
     *
     * @param args the command line arguments provided upon execution
     */
    private static void parse(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(LIVES_OPTION)) {
                lives = Integer.parseInt(args[i + 1]);
                i++;
            } else if (args[i].equals(MAX_HINTS_OPTION)) {
                maxHints = Integer.parseInt(args[i + 1]);
                i++;
            } else {
                phraseSource = args[i];
            }
        }
    }

    /**
     * Throws exceptions if options are invalid.
     *
     * @throws NumberFormatException if the amount of lives is not positive
     * @throws NumberFormatException if the amount of hints is negative
     */
    private static void validateOptions() throws NumberFormatException {
        if (lives < 1) {
            throw new NumberFormatException(INVALID_LIVES);
        }
        if (maxHints < 0) {
            throw new NumberFormatException(INVALID_HINTS);
        }
    }

    /** Hides the constructor for this utility class. */
    private OptionsParser() {
    }

}
