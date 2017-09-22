package main.cs5340.alafleur;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
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

    /**
     * Prints out the expected output using the Viterbi Algorithm on the given sentence.
     *
     * @param sentence - String the sentence to process
     */
    public void processSentence(String sentence) {
        if (probabilitiesManager == null) throw new RuntimeException("Probabilities manager has not been set!");

        System.out.println("PROCESSING SENTENCE: " + sentence + "\n");

        // Setup our variables for the Viterbi Algorithm
        ArrayList<ArrayList<Triplet<String, PartOfSpeech, PartOfSpeech>>> backptrNetwork = new ArrayList<>(); // Word, past PoS, this PoS
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

            ArrayList<Triplet<String, PartOfSpeech, PartOfSpeech>> backptr = new ArrayList<>();

            for (PartOfSpeech pos : PartOfSpeech.values()) {
                if (pos.equals(PartOfSpeech.PHI))
                    continue;

                double maxValue = Double.NEGATIVE_INFINITY;
                PartOfSpeech bestPastPos = null;
                for(Map.Entry<PartOfSpeech, Double> e : scores.get(i - 1).entrySet()) {
                    double value = logProb(probabilitiesManager.getTransitionProbabilityOf(pos, e.getKey())) +
                            e.getValue();

                    if (value > maxValue) {
                        maxValue = value;
                        bestPastPos = e.getKey();
                    }
                }

                double score = logProb(probabilitiesManager.getEmissionProbabilityOf(word, pos)) + maxValue;
                map.put(pos, score);

                // Manage the back pointer
                backptr.add(new Triplet<>(word, pos, bestPastPos));
            }

            scores.add(map);
            backptrNetwork.add(backptr);
        }

        System.out.println("FINAL VITERBI NETWORK");
        for (int i = 0; i < wordsInSentence.length; i++) {
            String word = wordsInSentence[i];
            TreeMap<PartOfSpeech, Double> m = scores.get(i);

            for (Map.Entry<PartOfSpeech, Double> e : m.entrySet())
                System.out.println("P(" + word.toLowerCase() + "=" + e.getKey().toString().toLowerCase() + ") = "
                        + decimalFormat.format(e.getValue()));
        }

        // Print out the back pointer network
        System.out.println("\nFINAL BACKPTR NETWORK");
        for (ArrayList<Triplet<String, PartOfSpeech, PartOfSpeech>> list : backptrNetwork) {
            for(Triplet<String, PartOfSpeech, PartOfSpeech> word : list) {
                System.out.println("Backptr(" + word.getObj1() + "=" + word.getObj2() + ") = " + word.getObj3());
            }
        }

        // Print out the best tag sequence probability
        double bestSequenceLogProb = 0.0;
        for (TreeMap<PartOfSpeech, Double> m : scores) {
            double greatestProb = Double.NEGATIVE_INFINITY;

            for (double d : m.values()) {
                if (d > greatestProb)
                    greatestProb = d;
            }

            bestSequenceLogProb += greatestProb;
        }

        // TODO: Ask about this - we're so close!!!
        System.out.println("\nBEST TAG SEQUENCE HAS LOG PROBABILITY = " + bestSequenceLogProb);
        for (int i = scores.size() - 1; i >= 0; i--) {
            String word = wordsInSentence[i];

            double bestScore = Double.NEGATIVE_INFINITY;
            PartOfSpeech bestPos = null;

            for (Map.Entry<PartOfSpeech, Double> entry : scores.get(i).entrySet()) {
                if (entry.getValue() > bestScore) {
                    bestPos = entry.getKey();
                    bestScore = entry.getValue();
                }
            }

            System.out.println(word + " -> " + bestPos);
        }


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
