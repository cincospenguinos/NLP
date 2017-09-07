package com.lafleur;

import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * Represents an NGram statistical model.
 */
public class NGramModel {

    private HashSet<NGram> vocabulary;
    private TreeMap<NGram, Integer> frequencyTable;
    private int valueOfN;
    private boolean withSmoothing;

    public NGramModel(int _valueOfN, boolean smoothing) {
        vocabulary = new HashSet<>();
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
            frequencyTable.put(ngram, frequencyTable.get(ngram) + 1);
        } else {
            frequencyTable.put(ngram, 1);
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
    public int totalFrequency(){
        int count = 0;

        for(int i : frequencyTable.values())
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
    public double probabilityOfSentence(String sentence){

        return 0.0; // TODO: This
    }
}
