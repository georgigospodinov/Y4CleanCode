package uk.ac.standrews.cs5031;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.Random;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * This class provides methods to select phrases for the Hangman game.
 *
 * @author Edwin Brady, 150009974
 * @version 3.0
 */
public final class Phrases {

    /** A generator for indexes within the arrays of predefined phrases. */
    public static final Random GENERATOR = new Random();

    /** The id of the Custom category. */
    public static final int CUSTOM_ID = 0;

    /** The id of the Countries category. */
    public static final int COUNTRIES_ID = 1;

    /** The id of the Counties category. */
    public static final int COUNTIES_ID = 2;

    /** The id of the Cities category. */
    public static final int CITIES_ID = 3;

    /** Custom phrases loaded from file. */
    private static String[] custom;

    /** Countries available by default. */
    static final String[] COUNTRIES = {"Scotland", "England", "Wales",
            "Northern Ireland", "Ireland", "France", "Germany", "Netherlands",
            "Spain", "Portugal", "Belgium", "Luxembourg", "Switzerland",
            "Italy", "Greece"};

    /** Counties available by default. */
    static final String[] COUNTIES = {"Argyll and Bute", "Caithness",
            "Kingdom of Fife", "East Lothian", "Highland",
            "Dumfries and Galloway", "Renfrewshire", "Scottish Borders",
            "Perth and Kinross"};

    /** Cities available by default. */
    static final String[] CITIES = {"St Andrews", "Edinburgh",
            "Glasgow", "Kirkcaldy", "Perth", "Dundee", "Stirling", "Inverness",
            "Aberdeen", "Falkirk"};

    /**
     * Loads the phrases from the phrases source file
     * if such was supplied upon running the system.
     *
     * @throws FileNotFoundException if the phrase source file is invalid
     */
    public static void loadCustom() throws FileNotFoundException {
        if (!OptionsParser.areThereCustomPhrases()) {
            custom = null;
            return;
        }
        String source = OptionsParser.getPhraseSource();
        Scanner reader = new Scanner(new FileInputStream(source), "utf-8");
        LinkedHashSet<String> customPhrases = new LinkedHashSet<>();
        while (reader.hasNextLine()) {
            customPhrases.add(reader.nextLine().trim());
        }
        reader.close();
        if (customPhrases.isEmpty()) {
            throw new FileNotFoundException("The phrase source was empty.");
        }
        custom = new String[customPhrases.size()];
        custom = customPhrases.toArray(custom);
    }

    /**
     * Retrieves a random phrase from the specified category.
     *
     * @param category the category id
     * @return a random phrase from the selected category
     */
    public static String getRandomPhrase(final int category) {
        int index;
        switch (category) {
            case CUSTOM_ID:
                if (custom == null) {
                    String message = "No custom phrases are provided";
                    throw new IllegalArgumentException(message);
                } else {
                    index = GENERATOR.nextInt(custom.length);
                    return custom[index];
                }
            case COUNTRIES_ID:
                index = GENERATOR.nextInt(COUNTRIES.length);
                return COUNTRIES[index];
            case COUNTIES_ID:
                index = GENERATOR.nextInt(COUNTIES.length);
                return COUNTIES[index];
            case CITIES_ID:
                index = GENERATOR.nextInt(CITIES.length);
                return CITIES[index];
            default:
                throw new IllegalArgumentException();
        }
    }

    /** Hides the constructor for this utility class. */
    private Phrases() {
    }

}
