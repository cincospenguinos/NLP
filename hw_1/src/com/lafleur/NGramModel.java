package com.lafleur;

import java.util.*;

/**
 * Represents an NGram statistical model.
 */
public class NGramModel {

    private TreeSet<NGram> vocabulary;
    private TreeMap<NGram, Double> frequencyTable;
    private int valueOfN;
    private boolean withSmoothing;

    public NGramModel(int _valueOfN, boolean smoothing) {
        vocabulary = new TreeSet<>();
        frequencyTable = new TreeMap<>();
        valueOfN = _valueOfN;
        withSmoothing = smoothing;
    }

    /**
     * Adds the NGram provided to this model.
     *
     * @param ngram - NGram to add
     */
    public void add(NGram ngram){
        if (ngram.size() != valueOfN)
            throw new RuntimeException("The provided NGram is " + ngram.size() + "; expected " + valueOfN);

        vocabulary.add(ngram);

        if (frequencyTable.containsKey(ngram)){
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
        for(NGram n : list)
            add(n);
    }

    /**
     * Returns the sum of all the frequencies for each word included in the frequency table.
     *
     * @return int - total frequency
     */
    public double totalFrequency(){
        double count = 0;

        for(double i : frequencyTable.values())
            count += i;

        return count;
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
                if (frequencyTable.containsKey(g))
                    product += lg(frequencyTable.get(g) / totalFrequency());
            }
        } else {
            for (NGram g : nGramsInSentence) {
                // Gather all words that begin with the first word of the NGram
                String firstWord = g.getNthWord(1);
                ArrayList<Map.Entry<NGram, Double>> list = new ArrayList<>();
                for(Map.Entry<NGram, Double> e : frequencyTable.entrySet()){

                    if (e.getKey().isNthWord(firstWord, 1))
                        list.add(e);
                }

                // Next, calculate how often the first word appears, and how often the first
                // and second appear
                String secondWord = g.getNthWord(2);

                double frequencyOfFirst = 0.0;
                double frequencyOfBoth = 0.0;

                for(Map.Entry<NGram, Double> e : list){
                    frequencyOfFirst += e.getValue();

                    if (e.getKey().isNthWord(secondWord, 2))
                        frequencyOfBoth += e.getValue();
                }

                // Now we calculate the log prob of our conditional probability
                product += lg(frequencyOfBoth / frequencyOfFirst);
            }
        }

        if (product == 0.0) return Double.NEGATIVE_INFINITY;

        return product;
    }

    /**
     * Returns the true probability of the log probability provided. For debugging purposes.
     *
     * @param logProb - the logarithm probability
     * @return the true probability
     */
    public double trueProb(double logProb) {
        if (logProb == Double.NEGATIVE_INFINITY)
            return 0.0;

        return Math.pow(2, logProb);
    }

    /**
     * Helper method. Returns the log base 2 of the number provided.
     *
     * @param number - the number to get the log of
     * @return The log of number with a base of 2
     */
    private double lg(double number){
        if (number == 0.0) return 0.0;
        return Math.log(number) / Math.log(2);
    }

    public int getValueOfN() {
        return valueOfN;
    }
}
