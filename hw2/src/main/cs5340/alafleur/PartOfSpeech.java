package main.cs5340.alafleur;

import org.jetbrains.annotations.Nullable;

/**
 * A single part of speech in a sentence.
 */
public enum PartOfSpeech {

    NOUN, VERB, INF, PREP, PHI;

    private static final String STRING_PHI = "phi";

    @Override
    public String toString() { // TODO: Add these as they show up
        switch(this) {
            case NOUN:
                return "NOUN";
            case VERB:
                return "VERB";
            case INF:
                return "INF";
            case PREP:
                return "PREP";
        }

        return super.toString();
    }

    public static boolean isPartOfSpeechOrPhi(String word) {
        return isPartOfSpeech(word) || isPhi(word);
    }

    /**
     * Returns true if the word provided is a part of speech.
     *
     * @param word - word we are checking the part of speech for
     * @return true if it's a part of speech
     */
    public static boolean isPartOfSpeech(String word) {
        String[] partsOfSpeech = {"inf", "verb", "noun", "prep"};

        for(String s : partsOfSpeech)
            if (s.equalsIgnoreCase(word))
                return true;

        return false;
    }

    /**
     * Returns true if the word provided is Phi.
     * @param word - String to check
     * @return true if the word is phi
     */
    public static boolean isPhi(String word) {
        return STRING_PHI.equalsIgnoreCase(word);
    }

    @Nullable
    public static PartOfSpeech toPartOfSpeech(String word) {
        if (!isPartOfSpeech(word))
            throw new RuntimeException("\"" + word + "\" is not a part of speech.");

        if (word.equalsIgnoreCase("inf"))
            return INF;
        if (word.equalsIgnoreCase("verb"))
            return VERB;
        if (word.equalsIgnoreCase("noun"))
            return NOUN;
        if (word.equalsIgnoreCase("prep"))
            return PREP;

        return null; // TODO: Maybe not return null?
    }
}
