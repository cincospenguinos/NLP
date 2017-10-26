package main.cs5340.alafleur;

import java.util.HashMap;

/**
 * Manages assigning IDs to various features and things
 */
public class FeatureManager {

    private static final int ABBR_ID = 1;
    private static final int CAP_ID = 2;
    private static final int LOCATION_ID = 3;
    private int counter;
    private HashMap<FeatureType, HashMap<Object, Integer>> features;
    private static final String[] LABEL_IDS = new String[] { "O", "B-PER", "I-PER", "B-LOC", "I-LOC", "B-ORG", "I-ORG" };

    public FeatureManager() {
        counter = 4;
        features = new HashMap<>();

        for (FeatureType type : FeatureType.values())
            features.put(type, new HashMap<Object, Integer>());
    }

    /**
     * Adds the feature provided to the collection.
     * @param type
     * @param item
     */
    private void addFeature(FeatureType type, Object item) {
        HashMap<Object, Integer> map = features.get(type);

        if (!map.containsKey(item))
            map.put(item, counter++);
    }

    /**
     * Returns the feature ID for the object passed given the feature type provided.
     * @param type
     * @param item
     * @return
     */
    public int getFeatureId(FeatureType type, Object item) {
        switch(type) {
            case ABBR:
                return ABBR_ID;
            case CAP:
                return CAP_ID;
            case LOCATION:
                return LOCATION_ID;
            default:
                HashMap<Object, Integer> map = features.get(type);

                if (map.containsKey(item))
                    return map.get(item);
                else {
                    addFeature(type, item);
                    return getFeatureId(type, item);
                }
        }
    }

    public int getLabelId(String label) {
        for (int i = 0; i < LABEL_IDS.length; i++) {
            if (label.equals(LABEL_IDS[i]))
                return i;
        }

        throw new RuntimeException("No label ID for " + label);
    }
}
