package main.cs5340.alafleur;

import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Represents a single word from the file. Contains feature information.
 */
public class Word {

    // Definitions
    public static final String PHI = "PHI";
    public static final String OMEGA = "OMEGA";

    public static final String PHI_POS = "PHIPOS";
    public static final String OMEGA_POS = "OMEGAPOS";

    public static final String UNK_WORD = "UNK";
    public static final String UNK_POS = "UNKPOS";

    private static final Pattern abbreviationPattern = Pattern.compile("[a-zA-Z\\.]*\\.");

    private static TreeSet<String> locations;

    // The features
    private String word;
    private String[] wordCon;
    private String partOfSpeech;
    private String[] partOfSpeechCon;
    private boolean isAbbreviation;
    private boolean isCapitalized;
    private boolean isLocation;

    // The label
    private String label;

    public Word (String prev, String thisWord, String next, String prevPos, String thisPos, String nextPos, String _label) {
        if (locations == null)
            throw new RuntimeException("Must set locations before attempting to instantiate a word!");

        word = thisWord;
        wordCon = new String[] { prev, next };
        partOfSpeech = thisPos;
        partOfSpeechCon = new String[] { prevPos, nextPos };
        isAbbreviation = isAbbreviation();
        isCapitalized = Character.isUpperCase(word.charAt(0));
        isLocation = locations.contains(word);

        label = _label;
    }

    /**
     * Returns the feature requested associated with this word.
     *
     * @param type - Type of feature to return
     * @return Object - That depends on the feature
     */
    public String getFeatureString(FeatureType type) {
        switch(type) {
            case WORD:
                return word;
            case WORDCON:
                return wordCon[0] + " " + wordCon[1];
            case POS:
                return partOfSpeech;
            case POSCON:
                return partOfSpeechCon[0] + " " + partOfSpeechCon[1];
            case ABBR:
                if (isAbbreviation)
                    return "yes";
                else
                    return "no";
            case CAP:
                if (isCapitalized)
                    return "yes";
                else
                    return "no";
            case LOCATION:
                if (isLocation)
                    return "yes";
                else
                    return "no";
        }

        return null;
    }

    /**
     * Gets the actual feature value associated with the type requested. Could be any number of things
     *
     * @param type - the type to get
     * @return String, String[], boolean, or null.
     */
    public Object getFeatureValue(FeatureType type) {
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
                return isAbbreviation;
            case CAP:
                return isCapitalized;
            case LOCATION:
                return isLocation;
        }

        return null;
    }

    public String toString() {
        return word;
    }

    public static void setLocations(TreeSet<String> locs) {
        locations = locs;
    }

    private boolean isAbbreviation() {
        return abbreviationPattern.matcher(word).matches() && word.length() <= 4;
    }

    public String getLabel() {
        return label;
    }
}
