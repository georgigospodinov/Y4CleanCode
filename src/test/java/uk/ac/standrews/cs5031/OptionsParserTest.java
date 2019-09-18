package uk.ac.standrews.cs5031;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.ac.standrews.cs5031.OptionsParser.LIVES_OPTION;
import static uk.ac.standrews.cs5031.OptionsParser.DEFAULT_LIVES;
import static uk.ac.standrews.cs5031.OptionsParser.MAX_HINTS_OPTION;
import static uk.ac.standrews.cs5031.OptionsParser.DEFAULT_MAX_HINTS;
import static uk.ac.standrews.cs5031.OptionsParser.NO_PHRASE_SOURCE;

import org.junit.Test;

/**
 * A test suite for {@link OptionsParser}.
 *
 * @author Edwin Brady, 150009974
 * @version 2.7
 */
public class OptionsParserTest {

    /**
     * When the '--lives' option is not used,
     * the system should resort to the default amount.
     */
    @Test
    public void providedNoLives() {
        OptionsParser.parseAndValidate(new String[]{});
        assertEquals(DEFAULT_LIVES, OptionsParser.getLives());
    }

    /**
     * When a positive amount of lives is provided,
     * the system should use that amount.
     */
    @Test
    public void providedLives() {
        int lives = 2;
        String[] args = {LIVES_OPTION, String.valueOf(lives)};
        OptionsParser.parseAndValidate(args);
        assertEquals(lives, OptionsParser.getLives());
    }

    /**
     * When zero lives are provided,
     * the system should throw an exception.
     */
    @Test(expected = NumberFormatException.class)
    public void providedZeroLives() {
        String[] args = {LIVES_OPTION, "0"};
        OptionsParser.parseAndValidate(args);
    }

    /**
     * When negative lives are provided,
     * the system should throw an exception.
     */
    @Test(expected = NumberFormatException.class)
    public void providedNegativeLives() {
        String[] args = {LIVES_OPTION, "-1"};
        OptionsParser.parseAndValidate(args);
    }

    /**
     * When the '--lives' option is used but the argument is not a number,
     * the system should throw an exception.
     */
    @Test(expected = NumberFormatException.class)
    public void providedBadFormatLives() {
        String[] args = {LIVES_OPTION, "not a number"};
        OptionsParser.parseAndValidate(args);
    }

    /**
     * When the '--max-hints' option is not used,
     * the system should resort to the default amount.
     */
    @Test
    public void providedNoHints() {
        OptionsParser.parseAndValidate(new String[]{});
        assertEquals(DEFAULT_MAX_HINTS, OptionsParser.getMaxHints());
    }

    /**
     * When a positive amount of hints is provided,
     * the system should use that amount.
     */
    @Test
    public void providedMaxHints() {
        int maxHints = 2;
        String[] args = {MAX_HINTS_OPTION, String.valueOf(maxHints)};
        OptionsParser.parseAndValidate(args);
        assertEquals(maxHints, OptionsParser.getMaxHints());
    }

    /** When zero hints are provided, the system should use that amount. */
    @Test
    public void providedZeroMaxHints() {
        int maxHints = 0;
        String[] args = {MAX_HINTS_OPTION, String.valueOf(maxHints)};
        OptionsParser.parseAndValidate(args);
        assertEquals(maxHints, OptionsParser.getMaxHints());
    }

    /**
     * When negative max hints are provided,
     * the system should throw an exception.
     */
    @Test(expected = NumberFormatException.class)
    public void providedNegativeMaxHints() {
        String[] args = {MAX_HINTS_OPTION, "-1"};
        OptionsParser.parseAndValidate(args);
    }

    /**
     * When the '--max-hints' option is used but the argument is not a number,
     * the system should throw an exception.
     */
    @Test(expected = NumberFormatException.class)
    public void providedBadFormatMaxHints() {
        String[] args = {MAX_HINTS_OPTION, "not a number"};
        OptionsParser.parseAndValidate(args);
    }

    /** When custom phrases not provided, the system should recognize that. */
    @Test
    public void providedNoPhraseSource() {
        OptionsParser.parseAndValidate(new String[]{});
        assertEquals(NO_PHRASE_SOURCE, OptionsParser.getPhraseSource());
        assertFalse(OptionsParser.areThereCustomPhrases());
    }

    /** When a name of a file is provided, it should be saved. */
    @Test
    public void providedPhraseSource() {
        String phraseSource = "resources/phrase_sources/valid.txt";
        String[] args = {phraseSource};
        OptionsParser.parseAndValidate(args);
        assertEquals(phraseSource, OptionsParser.getPhraseSource());
        assertTrue(OptionsParser.areThereCustomPhrases());
    }

    /**
     * When multiple options are set,
     * the system should correctly distinguish them.
     */
    @Test
    public void providedMultipleOptions() {
        int lives = 2;
        int maxHints = 1;
        String filename = "resources/phrase_sources/valid.txt";
        String[] args = {LIVES_OPTION, String.valueOf(lives), MAX_HINTS_OPTION,
                String.valueOf(maxHints), filename};
        OptionsParser.parseAndValidate(args);
        assertEquals(lives, OptionsParser.getLives());
        assertEquals(maxHints, OptionsParser.getMaxHints());
        assertEquals(filename, OptionsParser.getPhraseSource());
        assertTrue(OptionsParser.areThereCustomPhrases());
    }

}
