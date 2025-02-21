package com.lafleur;

import java.util.*;

/**
 * Represents an NGram statistical model.
 */
public class NGramModel {

    private TreeMap<NGram, Double> unigramFrequencyTable;
    private TreeMap<NGram, Double> frequencyTable;
    private double totalFrequency;

    private int valueOfN;
    private boolean withSmoothing;
    private boolean hasBeenSmoothed;

    public NGramModel(int _valueOfN, boolean smoothing) {
        frequencyTable = new TreeMap<>();
        unigramFrequencyTable = new TreeMap<>();
        totalFrequency = 0.0;
        valueOfN = _valueOfN;
        withSmoothing = smoothing;
        hasBeenSmoothed = false;
    }

    /**
     * Adds the NGram provided to this model.
     *
     * @param ngram - NGram to add
     */
    public void add(NGram ngram){
        if (ngram.size() != valueOfN)
            throw new RuntimeException("The provided NGram is " + ngram.size() + "; expected " + valueOfN);
        if (ngram.compareTo(NGram.PHI_NGRAM) == 0 && valueOfN == 1) // Do not add PHI if we are counting unigrams
            return;

        totalFrequency += 1.0;

        if (frequencyTable.containsKey(ngram)) {
            frequencyTable.put(ngram, frequencyTable.get(ngram) + 1.0);
        } else {
            frequencyTable.put(ngram, 1.0);
        }
    }

    /**
     * Adds all the NGrams provided in the list to this model.
     *
     * @param list - list of NGrams to add
     */
    public void addAll(List<NGram> list){
        for (int i = 0; i < list.size(); i += 2) {
            for (NGram unigram : list.get(i).getUnigrams()) {
                if (unigramFrequencyTable.containsKey(unigram))
                    unigramFrequencyTable.put(unigram, unigramFrequencyTable.get(unigram) + 1.0);
                else
                    unigramFrequencyTable.put(unigram, 1.0);
            }
        }

        for(NGram n : list)
            add(n);
    }

    /**
     * Returns the probability of the sentence provided given the sentence. This probability
     * is returned in log probability.
     *
     * @param sentence - Sentence to calculate the probability of
     * @return double, in log probability form
     */
    public double probabilityOfSentence(String sentence) {
        // Get all the NGrams in the provided sentence
        List<NGram> nGramsInSentence = NGram.getNGramsFromSentence(sentence, valueOfN);

        double product = 0.0;

        if (valueOfN == 1){
            for (NGram g : nGramsInSentence) {
                if (frequencyTable.containsKey(g)) {
                    double d = frequencyTable.get(g);
                    product += lg(frequencyTable.get(g) / totalFrequency);
                }
            }
        } else {
            for (NGram g : nGramsInSentence) {
                // Grab the frequency of the NGram g as a whole
                double frequencyOfNGram = 0.0;

                if (withSmoothing) {
                    if (frequencyTable.containsKey(g))
                        frequencyOfNGram = frequencyTable.get(g) + 1;
                    else
                        frequencyOfNGram = 1.0;
                } else if (frequencyTable.containsKey(g))
                    frequencyOfNGram = frequencyTable.get(g);

                // Grab the frequency of just the first word of g --> P(B & *)
                double frequencyOfFirstWord = unigramFrequencyTable.get(g.getUnigrams().get(0));

                // Divide the two and add them to product
                if (withSmoothing)
                    product += lg(frequencyOfNGram / (frequencyOfFirstWord + getUniqueUnigrams()));
                else
                    product += lg(frequencyOfNGram / frequencyOfFirstWord);;
            }
        }

        return product;
    }

    private double getUniqueUnigrams() {
        return (double) unigramFrequencyTable.keySet().size();
    }

    /**
     * Helper method. Returns the log base 2 of the number provided.
     *
     * @param number - the number to get the log of
     * @return The log of number with a base of 2
     */
    private double lg(double number){
        return Math.log(number) / Math.log(2);
    }

    public int getValueOfN() {
        return valueOfN;
    }
}
