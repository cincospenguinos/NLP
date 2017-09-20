package main.cs5340.alafleur;

import java.util.TreeMap;

/**
 * Object that uses the Viterbi algorithm to generate part of speech tags for a given sentence.
 */
public class Viterbi {

    private ViterbiProbabilitiesManager probabilitiesManager;

    public Viterbi(ViterbiProbabilitiesManager _manager) {
        setProbabilitiesManager(_manager);
    }




    public void setProbabilitiesManager(ViterbiProbabilitiesManager manager) {
        probabilitiesManager = manager;
    }
}
