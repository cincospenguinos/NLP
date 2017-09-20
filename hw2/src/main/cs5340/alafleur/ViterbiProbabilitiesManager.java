package main.cs5340.alafleur;

import java.util.TreeMap;

/**
 * Helper class to manage probabilities for the Viterbi Algorithm.
 */
public class ViterbiProbabilitiesManager {

    private TreeMap<String, TreeMap<PartOfSpeech, Double>> emissionProbability;
    private TreeMap<PartOfSpeech, TreeMap<PartOfSpeech, Double>> transitionProbability;

    private static final double DEFAULT_PROBABILITY = 0.0001;

    public ViterbiProbabilitiesManager() {
        emissionProbability = new TreeMap<>();
        transitionProbability = new TreeMap<>();
    }

    /**
     * Adds an emission probability with the provided word, part of speech, and probability.
     *
     * @param word - String
     * @param pos - PartOfSpeech
     * @param prob - double probability
     */
    public void addEmissionProbability(String word, PartOfSpeech pos, double prob) {
        TreeMap<PartOfSpeech, Double> map;

        if (emissionProbability.containsKey(word)) {
            map = emissionProbability.get(word);
            map.put(pos, prob);
        } else {
            map = new TreeMap<>();
            map.put(pos, prob);

            emissionProbability.put(word, map);
        }
    }

    /**
     * Adds a transmission probability with the two parts of speech and the probability.
     *
     * @param pos1 - PartOfSpeech
     * @param pos2 - PartOfSpeech
     * @param prob - double probability
     */
    public void addTransmissionProbability(PartOfSpeech pos1, PartOfSpeech pos2, double prob) {
        TreeMap<PartOfSpeech, Double> map;

        if (transitionProbability.containsKey(pos1)) {
            map = transitionProbability.get(pos1);
            map.put(pos2, prob);
        } else {
            map = new TreeMap<>();
            map.put(pos2, prob);

            transitionProbability.put(pos1, map);
        }
    }

    /**
     * Returns the emission probability of the word - that is, the probability that the word
     * passed is the part of speech passed.
     *
     * @param word - String word to check
     * @param partOfSpeech - PartOfSpeech to check
     * @return double - probability of word | partOfSpeech
     */
    public double getEmissionProbabilityOf(String word, PartOfSpeech partOfSpeech) {
        word = word.toLowerCase();

        if (emissionProbability.containsKey(word)) {
            TreeMap<PartOfSpeech, Double> map = emissionProbability.get(word);

            if (map.containsKey(partOfSpeech))
                return map.get(partOfSpeech);
            else
                return DEFAULT_PROBABILITY;
        } else
            return DEFAULT_PROBABILITY;
    }

    /**
     * Returns the transition probability of the two parts of speech provided - that is, the probability
     * that pos1 appears given pos2.
     *
     * @param pos1 - PartOfSpeech visible
     * @param pos2 - PartOfSpeech just before pos1
     * @return double - probability of pos1 | pos2
     */
    public double getTransitionProbabilityOf(PartOfSpeech pos1, PartOfSpeech pos2) {
        if (transitionProbability.containsKey(pos1)) {
            TreeMap<PartOfSpeech, Double> map = transitionProbability.get(pos1);

            if (map.containsKey(pos2))
                return map.get(pos2);
            else
                return DEFAULT_PROBABILITY;
        } else
            return DEFAULT_PROBABILITY;
    }
}
