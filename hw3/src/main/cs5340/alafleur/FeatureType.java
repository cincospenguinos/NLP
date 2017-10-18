package main.cs5340.alafleur;

/**
 * Types of features available
 */
public enum FeatureType {
    WORD, WORDCON, POS, POSCON, ABBR, CAP, LOCATION;

    /**
     * Returns FeatureType matching string.
     *
     * @param str - The string to match
     * @return FeatureType or null
     */
    public static FeatureType fromString(String str) {
        for (FeatureType ftype : FeatureType.values())
            if (str.equals(ftype.toString()))
                return ftype;

        return null;
    }
}
