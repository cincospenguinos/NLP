package com.lafleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a single N-gram.
 */
public class NGram {

    public static final String PHI = ""; // PHI is the empty string - counts as the beginning of the sentence

    private String[] words;

    public NGram(String[] _words) {
        words = _words;
    }

    public int size() {
        return words.length;
    }

    public boolean equals(Object o) {
        if (o instanceof NGram) {
            NGram g = (NGram) o;

            if (this.size() != g.size())
                return false;

            for(int i = 0; i < this.size(); i++)
                if (!this.words[i].equalsIgnoreCase(g.words[i]))
                    return false;

            return true;
        }

        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(String w : words){
            builder.append("\"" + w + "\"");
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * Returns the complete collection of NGrams from the sentence provided with some size n.
     *
     * @param sentence - The sentence to pick apart
     * @param n - How big your NGram should be
     * @return List of NGrams from sentence
     */
    public static List<NGram> getNGramsFromSentence(String sentence, int n){
        List<NGram> nGrams = new ArrayList<>();
        String[] words = sentence.split(" ");

        if (words.length < n)
            throw new RuntimeException("Cannot provide a sentence greater than n!");

        if (n == 1){
            for (String word1 : words) {
                String[] word = new String[1];
                word[0] = word1;
                nGrams.add(new NGram(word));
            }
        } else {
            for (int i = 0; i < words.length; i++){
                String[] gram = new String[n];

                for (int j = 0; j < n; j++) {
                    int wordIndex = i - 1;

                    if (wordIndex == -1){
                        gram[j] = PHI;
                        continue;
                    }

                    gram[j] = words[wordIndex];
                }

                nGrams.add(new NGram(gram));
            }
        }

        return nGrams;
    }
}
