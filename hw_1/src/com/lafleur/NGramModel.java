package com.lafleur;

import java.util.*;

/**
 * Represents an NGram statistical model.
 */
public class NGramModel {

    private TreeMap<NGram, Double> frequencyTable;
    private TreeSet<String> vocabulary;
    private double smoothedTotalFrequency;
    private int valueOfN;
    private boolean withSmoothing;
    private boolean hasBeenSmoothed;

    public NGramModel(int _valueOfN, boolean smoothing) {
        frequencyTable = new TreeMap<>();
        valueOfN = _valueOfN;
        withSmoothing = smoothing;
        hasBeenSmoothed = false;
        vocabulary = new TreeSet<>();
    }

    /**
     * Adds the NGram provided to this model.
     *
     * @param ngram - NGram to add
     */
    public void add(NGram ngram){
        if (ngram.size() != valueOfN)
            throw new RuntimeException("The provided NGram is " + ngram.size() + "; expected " + valueOfN);

        for (int i = 1; i <= ngram.size(); i++)
            vocabulary.add(ngram.getNthWord(i));

        if (frequencyTable.containsKey(ngram)){
            frequencyTable.put(ngram, frequencyTable.get(ngram) + 1.0);
        } else {
            frequencyTable.put(ngram, 1.0);
        }

        if (ngram.compareTo(NGram.getNGramsFromSentence("Wolf", 1).get(0)) == 0 && valueOfN == 1) {
            double d = frequencyTable.get(ngram);
            d++;
        }
    }

    /**
     * Adds all the NGrams provided in the list to this model.
     *
     * @param list - list of NGrams to add
     */
    public void addAll(List<NGram> list){
        for(NGram n : list)
            add(n);
    }

    /**
     * Returns the sum of all the frequencies for each word included in the frequency table.
     *
     * @return int - total frequency
     */
    public double totalFrequency() {
        double count = 0;

        for(double i : frequencyTable.values())
            count += i;

        return count;
    }

    public double vocabSize() {
        return (double) vocabulary.size();
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
                    product += lg(frequencyTable.get(g) / totalFrequency());
                }
            }
        } else {
            for (NGram g : nGramsInSentence)
                product += logProbOfGivenPrevious(g);
        }

        return product;
    }

    /**
     * Smooths over this model, given that it has not been smoothed and is supposed to be smoothed.
     */
    public void smoothOver() {
        if (withSmoothing && !hasBeenSmoothed) {
            // Calculate the total number of bigrams that are in our "vocabulary"
            TreeSet<String> unigrams = new TreeSet<>();

            for (NGram nGram : frequencyTable.keySet())
                for (int i = 1; i <= nGram.size(); i++)
                    unigrams.add(nGram.getNthWord(i));

//            double totalBigrams = Math.pow((double) unigrams.size(), 2.0); // This is our value V - the number of new 1s
            double currentFrequency = totalFrequency(); // This is the old frequency count
            smoothedTotalFrequency = unigrams.size() + currentFrequency;

            for (Map.Entry<NGram, Double> e : frequencyTable.entrySet())
                frequencyTable.put(e.getKey(), (e.getValue() + 1.0) / smoothedTotalFrequency); // This is the smoothed total

            hasBeenSmoothed = true;
        }
    }

    /**
     * Returns the log probability of the nGram provided.
     *
     * @param nGram - NGram to get the log prob of
     * @return log prob of that nGram
     */
    private double logProbOf(NGram nGram) {
        if (withSmoothing && hasBeenSmoothed) {
            if (frequencyTable.containsKey(nGram))
                return lg(frequencyTable.get(nGram) / smoothedTotalFrequency);
            else
                return lg(1.0 / smoothedTotalFrequency);
        } else {
            if (frequencyTable.containsKey(nGram))
                return lg(frequencyTable.get(nGram) / totalFrequency());
            else
                return lg(0.0);
        }
    }

    /**
     * Returns the conditional log proability of the nGram provided.
     *
     * @param nGram - The nGram to check out
     * @return log prob of the nGram given the previous words
     */
    private double logProbOfGivenPrevious(NGram nGram) {
        double frequencyOfFirst = 0.0;

        for (NGram g : frequencyTable.keySet())
            if (g.isNthWord(nGram.getNthWord(1), 1))
                frequencyOfFirst += frequencyTable.get(g);

        if (withSmoothing && hasBeenSmoothed) // TODO: The issue is here. Talk to the TAs
            return logProbOf(nGram) - lg(frequencyOfFirst / smoothedTotalFrequency);
        else
            return logProbOf(nGram) - lg(frequencyOfFirst / totalFrequency());
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

    public TreeMap<NGram, Double> getFrequencyTable() {
        return frequencyTable;
    }
}
