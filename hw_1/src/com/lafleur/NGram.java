package com.lafleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a single N-gram.
 */
public class NGram implements Comparable {

    public static final String PHI = ""; // PHI is the empty string - counts as the beginning of the sentence

    private String[] words;

    public NGram(String[] _words) {
        for(int i = 0; i < _words.length; i++)
            _words[i] = _words[i].toLowerCase();
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
                if (!this.words[i].equals(g.words[i]))
                    return false;

            return true;
        }

        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < words.length - 1; i++) {
            String w = words[i];
            if (w.equals(PHI))
                builder.append("PHI, ");
            else
                builder.append("\"" + w + "\", ");
        }

        builder.append("\"" + words[words.length - 1] + "\"");
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
                    int wordIndex = i + j - (n - 1);

                    if (wordIndex < 0){
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

    @Override
    public int compareTo(Object o) {
        if (o instanceof NGram){
            NGram other = (NGram) o;

            if (other.size() != this.size())
                return -1;

            int compareValue = 0;

            for(int i = 0; i < other.size(); i++)
                compareValue += other.words[i].compareTo(this.words[i]);

            return compareValue;
        }

        return -1;
    }
}
