package main.cs5340.alafleur;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Object that uses the Viterbi algorithm to generate part of speech tags for a given sentence.
 */
public class Viterbi {

    private ViterbiProbabilitiesManager probabilitiesManager;
    private DecimalFormat decimalFormat;

    public Viterbi(ViterbiProbabilitiesManager _manager) {
        setProbabilitiesManager(_manager);
        decimalFormat = new DecimalFormat("###.####");
    }

    public void processSentence(String sentence) {
        if (probabilitiesManager == null) throw new RuntimeException("Probabilities manager has not been set!");

        System.out.println("PROCESSING SENTENCE: " + sentence + "\n");

        // Setup our variables for the Viterbi Algorithm
        // TODO: Add the back pointer datastructure
        String[] wordsInSentence = sentence.split("\\s+");

        ArrayList<TreeMap<PartOfSpeech, Double>> scores = new ArrayList<>();
        TreeMap<PartOfSpeech, Double> map = new TreeMap<>();

        // Calculate the scores for the PoS for the first word and store them
        for (PartOfSpeech pos : PartOfSpeech.values()) {
            if (pos.equals(PartOfSpeech.PHI))
                continue;
            double score = logProb(probabilitiesManager.getEmissionProbabilityOf(wordsInSentence[0], pos)) +
                    logProb(probabilitiesManager.getTransitionProbabilityOf(pos, PartOfSpeech.PHI));
            map.put(pos, score);
        }

        scores.add(map);

        // Calculate the scores for all the other words in the sentence and store them as well,
        // only considering the maximum probability
        for (int i = 1; i < wordsInSentence.length; i++) {
            String word = wordsInSentence[i];
            map = new TreeMap<>();
            PartOfSpeech bestPrevPos = getMaxPos(scores.get(i - 1));

            for (PartOfSpeech pos : PartOfSpeech.values()) { // TODO: Is this the correct calculation?
                if (pos.equals(PartOfSpeech.PHI))
                    continue;

                double score = logProb(probabilitiesManager.getEmissionProbabilityOf(word, pos)) +
                        logProb(probabilitiesManager.getTransitionProbabilityOf(pos, bestPrevPos)) +
                        scores.get(i - 1).get(bestPrevPos);
                map.put(pos, score);
            }

            scores.add(map);
        }

        System.out.println("FINAL VITERBI NETWORK");

        for (int i = 0; i < wordsInSentence.length; i++) {
            String word = wordsInSentence[i];
            TreeMap<PartOfSpeech, Double> m = scores.get(i);

            for (Map.Entry<PartOfSpeech, Double> e : m.entrySet())
                System.out.println("P(" + word.toLowerCase() + "=" + e.getKey().toString().toLowerCase() + ") = "
                        + decimalFormat.format(e.getValue()));
        }

        // Follow the back pointer to find the proper sequence

        System.out.println("\nFINAL BACKPTR NETWORK");

        System.out.println("\nBEST TAG SEQUENCE HAS LOG PROBABILITY = ");
        System.out.println("");
    }

    private PartOfSpeech getMaxPos(Map<PartOfSpeech, Double> map) {
        double max = Double.NEGATIVE_INFINITY;
        PartOfSpeech pos = null;

        for (Map.Entry<PartOfSpeech, Double> e : map.entrySet())
            if (e.getValue() > max) {
                pos = e.getKey();
                max = e.getValue();
            }


        return pos;
    }

    /**
     * Returns the log prob of the value provided.
     *
     * @param value - value to get the log prob of
     * @return double - log prob of value
     */
    public double logProb(double value) {
        return Math.log(value) / Math.log(2);
    }

    public void setProbabilitiesManager(ViterbiProbabilitiesManager manager) {
        probabilitiesManager = manager;
    }
}
