package uk.ac.standrews.cs5031;

import org.junit.Test;
import org.junit.Before;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;

import static org.junit.Assert.fail;

/**
 * A test suite for {@link Game}.
 *
 * @author Edwin Brady, 150009974
 * @version 2.0
 */
public class GameTest {

    /**
     * Redirects the standard output stream {@link System#out}, so that
     * the output can be tested.
     *
     * @throws UnsupportedEncodingException if the underlying PrintStream
     *                                      does not support utf-8.
     *                                      Should not happen.
     */
    @Before
    public void redirectOutput() throws UnsupportedEncodingException {
        TestingUtils.redirectOutput();
    }

    /**
     * Makes method calls to simulate a single play-through of the game.
     *
     * @param args the command line arguments for the execution
     * @throws FileNotFoundException if it fails to load
     *                               the custom phrase source
     */
    private void simulateExecution(final String[] args)
            throws FileNotFoundException {
        OptionsParser.parseAndValidate(args);
        Phrases.loadCustom();
        Game.play(null);
        Game.printGameOverMessage();
    }

    /**
     * Makes method calls to simulate a single play-through of the game
     * with default lives and hints but the phrase source file
     * "resources/phrase_sources/single.txt".
     *
     * This method is equivalent to
     * simulateExecution(new String[]{"resources/phrase_sources/single.txt"});
     *
     * @throws FileNotFoundException if it fails to load
     *                               the custom phrase source
     */
    private void simulateExecution() throws FileNotFoundException {
        simulateExecution(new String[]{"resources/phrase_sources/single.txt"});
    }

    /** Starting a game and not making any guesses causes an exception. */
    @Test(expected = NoSuchElementException.class)
    public void endOfInputCausesAnException() {
        TestingUtils.redirectInput("1\n");
        OptionsParser.parseAndValidate(new String[]{});
        Game.play(null);
    }

    /**
     * Spaces are automatically revealed.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void revealedSpaces()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\n");
        String filename = "resources/phrase_sources/multi_word_phrase.txt";
        String[] args = new String[]{filename};
        try {
            simulateExecution(args);
            fail("The execution should cause an exception given the input.");
        } catch (NoSuchElementException e) {
            // This exception is thrown by the Scanner object.
            // On end of input, the Scanner fails to read.
            // That is fine for this test.
        }
        TestingUtils.expectOutput("multi_word_phrase_auto_spaces.txt");
    }

    /**
     * Making only correct guesses outputs only 'Correct!' statements
     * and wins the game.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void onlyCorrect()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nW\no\nr\nd\n");
        simulateExecution();
        TestingUtils.expectOutput("single_only_correct.txt");
    }

    /**
     * Making only incorrect guesses outputs only 'Wrong!' statements
     * and looses the game.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void onlyWrong()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\na\nb\nc\ne\n");
        String filename = "resources/phrase_sources/single.txt";
        // 4 lives for the 4 guesses: a b c e.
        String[] args = new String[]{OptionsParser.LIVES_OPTION, "4", filename};
        simulateExecution(args);
        TestingUtils.expectOutput("single_only_wrong.txt");
    }

    /**
     * Correctly guessing the whole word should win the game.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void correctFullGuess()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nWord\n");
        simulateExecution();
        TestingUtils.expectOutput("single_correct_full_guess.txt");
    }

    /**
     * Incorrectly guessing the whole word multiple times should loose the game.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void incorrectFullGuesses()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nabc\nabc\nabc\n");
        String filename = "resources/phrase_sources/single.txt";
        // 3 lives for the 3 guesses
        String[] args = new String[]{OptionsParser.LIVES_OPTION, "3", filename};
        simulateExecution(args);
        TestingUtils.expectOutput("single_incorrect_full_guesses.txt");
    }

    /**
     * Repeatedly guessing the same letter has no effect.
     * The first mistake reduces the lives but
     * the repeated guesses have no effect.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void repeatLetterGuess()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\na\na\na\n");
        try {
            simulateExecution();
            fail("The execution should cause an exception given the input.");
        } catch (NoSuchElementException e) {
            // This exception is thrown by the Scanner object.
            // On end of input, the Scanner fails to read.
            // That is fine for this test.
        }
        TestingUtils.expectOutput("single_repeat_letter.txt");
    }

    /**
     * Not making a guess and pressing Enter results in an 'empty' guess
     * which should be ignored.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void emptyGuess()
        throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\n\n\nWord\nn\n");
        simulateExecution();
        TestingUtils.expectOutput("single_empty_guess.txt");
    }

    /**
     * Exiting early from the game does not output a Game Over message.
     * Early exit is when the input ends but the player
     * still has lives and more letters to guess.
     * In other words, neither won nor lost.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void noMessageOnEarlyExit()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\na\n");
        String[] args = new String[]{"resources/phrase_sources/single.txt"};
        OptionsParser.parseAndValidate(args);
        Phrases.loadCustom();
        try {
            Game.play(null);
            fail("The execution should cause an exception given the input.");
        } catch (NoSuchElementException e) {
            // This exception is thrown by the Scanner object.
            // On end of input, the Scanner fails to read.
            // That is fine for this test.
        }
        Game.printGameOverMessage();
        TestingUtils.expectOutput("single_early_exit.txt");
    }

    /**
     * Asking for hints provides appropriate hints.
     * This test is on a phrase with a single letter,
     * so that the hint is always the same.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void canAskForHints()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\n?\na\n");
        String[] args = new String[]{"resources/phrase_sources/one_letter.txt"};
        simulateExecution(args);
        TestingUtils.expectOutput("one_letter_ask_for_hints.txt");
    }

    /**
     * No hints are given once all are used up.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void runOutOfHints()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\n?\n?\n?\n?\n");
        String[] args = new String[]{"resources/phrase_sources/one_letter.txt"};
        try {
            simulateExecution(args);
            fail("The execution should cause an exception given the input.");
        } catch (NoSuchElementException e) {
            // This exception is thrown by the Scanner object.
            // On end of input, the Scanner fails to read.
            // That is fine for this test.
        }
        TestingUtils.expectOutput("one_letter_run_out_of_hints.txt");
    }

    /**
     * Guesses should be case-insensitive.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void caseInsensitive()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nw\nO\nworLd\nwoRd\n");
        simulateExecution();
        TestingUtils.expectOutput("single_case_insensitive.txt");
    }

    /**
     * Invalid category is not accepted.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void invalidCategoryID()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("-1\n0\nWord\n");
        simulateExecution();
        TestingUtils.expectOutput("single_category_not_recognized.txt");
    }

    /**
     * Invalid category is not accepted.
     *
     * @throws FileNotFoundException        if it fails to load the custom
     *                                      phrase source file or
     *                                      the expected output file.
     *                                      Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void notNumberCategory()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("a\n0\nWord\n");
        simulateExecution();
        TestingUtils.expectOutput("single_category_not_recognized.txt");
    }

}
