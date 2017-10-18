package main.cs5340.alafleur;

import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Represents a single word from the file. Contains feature information.
 */
public class Word {

    // Definitions
    public static final String PHI = "\\PHI\\";
    public static final String OMEGA = "\\OMEGA\\";

    public static final String PHI_POS = "\\PHI_POS\\";
    public static final String OMEGA_POS = "\\OMEGA_POS\\";

    private static final Pattern abbreviationPattern = Pattern.compile("[a-zA-Z\\.]+\\.");

    private static TreeSet<String> locations;

    // The features
    private String word;
    private String[] wordCon;
    private String partOfSpeech;
    private String[] partOfSpeechCon;
    private boolean abbreviation;
    private boolean capitalized;
    private boolean location;

    public Word (String prev, String thisWord, String next, String prevPos, String thisPos, String nextPos) {
        if (locations == null)
            throw new RuntimeException("Must set locations before attempting to instantiate a word!");

        word = thisWord;
        wordCon = new String[] { prev, next };
        partOfSpeech = thisPos;
        partOfSpeechCon = new String[] { prevPos, nextPos };
        abbreviation = isAbbreviation();
        capitalized = Character.isUpperCase(word.charAt(0));
        location = locations.contains(word);
    }

    /**
     * Returns the feature requested associated with this word.
     *
     * @param type - Type of feature to return
     * @return Object - That depends on the feature
     */
    public Object getFeature(FeatureType type) {
        switch(type) {
            case WORD:
                return word;
            case WORDCON:
                return wordCon;
            case POS:
                return partOfSpeech;
            case POSCON:
                return partOfSpeechCon;
            case ABBR:
                return abbreviation;
            case CAP:
                return capitalized;
            case LOCATION:
                return location;
        }

        return null;
    }

    public static void setLocations(TreeSet<String> locs) {
        locations = locs;
    }

    private boolean isAbbreviation() {
        return abbreviationPattern.matcher(word).matches() && word.length() <= 4;
    }
}
