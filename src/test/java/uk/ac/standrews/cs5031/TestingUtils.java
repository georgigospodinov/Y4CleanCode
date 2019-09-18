package uk.ac.standrews.cs5031;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Contains several methods to support testing.
 *
 * @author 150009974
 * @version 1.1
 */
public final class TestingUtils {

    /** The folder containing the files with the expected output. */
    private static final String EXPECTED_OUTPUTS_FOLDER =
            "resources/expected_outputs/";

    /** Encoding to use when accessing files. */
    private static final String UTF_8 = "utf-8";

    /** End of file unicode character used when reading expected output. */
    private static final String EOF = "\u001a";

    /** This stream replaces the Standard Output, so as to test output. */
    private static ByteArrayOutputStream out;

    /** Hides the constructor for this utility class. */
    private TestingUtils() {
    }

    /**
     * Redirects the standard output stream {@link System#out}, so that
     * the output can be tested.
     *
     * @throws UnsupportedEncodingException if the underlying PrintStream
     *                                      does not support utf-8.
     *                                      Should not happen.
     */
    public static void redirectOutput() throws UnsupportedEncodingException {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, UTF_8));
    }

    /**
     * Redirects the standard input stream so that it accepts the given string
     * as input.
     *
     * @param input the string to be given as input
     */
    public static void redirectInput(final String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        System.setIn(in);
    }

    /**
     * Reads the contents of the specified file and
     * asserts that they equal the output in {@link TestingUtils#out}.
     * The file should be located in the folder containing the expected outputs.
     *
     * @param filename the name of the file containing the expected output
     * @throws FileNotFoundException        if the specified file is not found
     * @throws UnsupportedEncodingException if the String conversion does not
     *                                      support utf-8. Should not happen.
     * @see TestingUtils#EXPECTED_OUTPUTS_FOLDER
     */
    public static void expectOutput(final String filename)
            throws FileNotFoundException, UnsupportedEncodingException {
        String filePath = EXPECTED_OUTPUTS_FOLDER + filename;
        Scanner sc = new Scanner(new FileInputStream(filePath), UTF_8);
        sc.useDelimiter(EOF);
        String expected = sc.next();
        sc.close();
        String actual = out.toString(UTF_8);
        assertEquals(expected, actual);
    }

}
