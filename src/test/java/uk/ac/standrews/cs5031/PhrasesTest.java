package uk.ac.standrews.cs5031;

import org.junit.Test;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedHashSet;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertTrue;
import static uk.ac.standrews.cs5031.Phrases.COUNTRIES_ID;
import static uk.ac.standrews.cs5031.Phrases.COUNTRIES;
import static uk.ac.standrews.cs5031.Phrases.COUNTIES_ID;
import static uk.ac.standrews.cs5031.Phrases.COUNTIES;
import static uk.ac.standrews.cs5031.Phrases.CITIES_ID;
import static uk.ac.standrews.cs5031.Phrases.CITIES;
import static uk.ac.standrews.cs5031.Phrases.CUSTOM_ID;

/**
 * A test suite for {@link Phrases}.
 *
 * @author 150009974
 * @version 2.2
 */
public class PhrasesTest {

    /**
     * Retrieving a random phrase from the country category returns a country.
     */
    @Test
    public void getCountry() {
        String phrase = Phrases.getRandomPhrase(COUNTRIES_ID);
        List<String> countries = Arrays.asList(COUNTRIES);
        assertTrue(countries.contains(phrase));
    }

    /** Retrieving a random phrase from the county category returns a county. */
    @Test
    public void getCounty() {
        String phrase = Phrases.getRandomPhrase(COUNTIES_ID);
        List<String> counties = Arrays.asList(COUNTIES);
        assertTrue(counties.contains(phrase));
    }

    /** Retrieving a random phrase from the city category returns a city. */
    @Test
    public void getCity() {
        String phrase = Phrases.getRandomPhrase(CITIES_ID);
        List<String> cities = Arrays.asList(CITIES);
        assertTrue(cities.contains(phrase));
    }

    /** Retrieving a phrase from an invalid category causes an exception. */
    @Test(expected = IllegalArgumentException.class)
    public void getIllegal() {
        Phrases.getRandomPhrase(-1);
    }

    /**
     * Retrieving a random phrase from custom source when no such is provided
     * causes an exception.
     *
     * @throws IllegalArgumentException if the no phrase source is provided.
     *                                  Should happen.
     * @throws FileNotFoundException    if the provided phrase source file is
     *                                  invalid. Should not happen.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getCustomWhenNotProvided() throws FileNotFoundException {
        String[] args = {OptionsParser.NO_PHRASE_SOURCE};
        OptionsParser.parseAndValidate(args);
        Phrases.loadCustom();
        Phrases.getRandomPhrase(CUSTOM_ID);
    }

    /**
     * Retrieving a random phrase from a valid file returns
     * a phrase that is indeed in that file.
     *
     * @throws IOException if the test file is invalid (Should not happen)
     *                     or if closing the reader fails
     */
    @Test
    public void getCustomFromValidFile() throws IOException {
        String filename = "resources/phrase_sources/valid.txt";
        OptionsParser.parseAndValidate(new String[]{filename});
        Phrases.loadCustom();
        String phrase = Phrases.getRandomPhrase(CUSTOM_ID);
        FileInputStream fis = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(fis, UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        LinkedHashSet<String> lines = new LinkedHashSet<>();
        reader.lines().forEach(line -> lines.add(line.trim()));
        reader.close();
        assertTrue(lines.contains(phrase));
    }

    /**
     * Retrieving a random phrase from an empty file causes an exception.
     *
     * @throws FileNotFoundException if the test file is invalid.
     *                               Should happen.
     */
    @Test(expected = FileNotFoundException.class)
    public void getCustomFromEmptyFile() throws FileNotFoundException {
        String filename = "resources/phrase_sources/empty.txt";
        OptionsParser.parseAndValidate(new String[]{filename});
        Phrases.loadCustom();
    }

    /**
     * Retrieving a random phrase from a non-existent file causes an exception.
     *
     * @throws FileNotFoundException if the test file is invalid.
     *                               Should happen.
     */
    @Test(expected = FileNotFoundException.class)
    public void getCustomFromNonExistentFile() throws FileNotFoundException {
        String filename = "non_existent.txt";
        OptionsParser.parseAndValidate(new String[]{filename});
        Phrases.loadCustom();
    }

}
