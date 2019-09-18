package uk.ac.standrews.cs5031;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;

/**
 * A test suite for {@link Hangman}.
 *
 * @author 150009974
 * @version 1.4
 */
public class HangmanTest {

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
     * Executes the main method of the system with the given arguments.
     *
     * @param args the command line arguments for the execution
     */
    private void simulateExecution(final String[] args) {
        Hangman.main(args);
    }

    /**
     * Executes the main method of the system
     * with default lives and hints but the phrase source file
     * "resources/phrase_sources/single.txt".
     *
     * This method is equivalent to
     * simulateExecution(new String[]{"resources/phrase_sources/single.txt"});
     */
    private void simulateExecution() {
        simulateExecution(new String[]{"resources/phrase_sources/single.txt"});
    }

    /**
     * When the amount of lives is invalid, an error message should be outputted
     * and the system should exit.
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void invalidLivesArgument()
            throws FileNotFoundException, UnsupportedEncodingException {
        String[] args = new String[]{OptionsParser.LIVES_OPTION, "-1"};
        simulateExecution(args);
        TestingUtils.expectOutput("execution_invalid_lives.txt");
    }

    /**
     * When the amount of hints is invalid, an error message should be outputted
     * and the system should exit.
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void invalidHintsArgument()
            throws FileNotFoundException, UnsupportedEncodingException {
        String[] args = new String[]{OptionsParser.MAX_HINTS_OPTION, "-1"};
        simulateExecution(args);
        TestingUtils.expectOutput("execution_invalid_hints.txt");
    }

    /**
     * When the player answer with "no" to the 'play again' question,
     * the system should exit.
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void endOnNo()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nWord\nno\n");
        simulateExecution();
        TestingUtils.expectOutput("execution_ends_on_no.txt");
    }

    /**
     * When the player answer with "yes" to the 'play again' question,
     * the system should start a new game.
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void repeatOnYes()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nWord\nyes\n0\nWord\nyes\n0\nWord\nno\n");
        simulateExecution();
        TestingUtils.expectOutput("execution_repeat_on_yes.txt");
    }

    /**
     * "Yep" and "Nope" are accepted as "yes" and "no".
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void yepNopeAnswers()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nWord\nYep\n0\nWord\nNope\n");
        simulateExecution();
        TestingUtils.expectOutput("execution_yep_nope.txt");
    }

    /**
     * "Y" and "N" are accepted as "yes" and "no".
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void yNAnswers()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nWord\nY\n0\nWord\nN\n");
        simulateExecution();
        TestingUtils.expectOutput("execution_y_n.txt");
    }

    /**
     * "True" and "False" are accepted as "yes" and "no".
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void trueFalseAnswers()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nWord\ntrue\n0\nWord\nfalse\n");
        simulateExecution();
        TestingUtils.expectOutput("execution_true_false.txt");
    }

    /**
     * Unrecognized answer causes a re-prompt.
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void unrecognizedAnswer()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("0\nWord\ninvalid\nanswers\nno\n");
        simulateExecution();
        TestingUtils.expectOutput("execution_invalid_answer.txt");
    }

    /**
     * When the file with custom phrases is not found,
     * the system should still allow choosing from other categories.
     *
     * @throws FileNotFoundException        if the expected output file
     *                                      is not found. Should not happen.
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     */
    @Test
    public void missingCustomPhrases()
            throws FileNotFoundException, UnsupportedEncodingException {
        TestingUtils.redirectInput("");
        simulateExecution(new String[]{"invalid_file.txt"});
        TestingUtils.expectOutput("missing_custom_phrases.txt");
    }

}
