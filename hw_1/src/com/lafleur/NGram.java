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

    /**
     * Returns true if word is the nth word in this NGram.
     *
     * @param word - Word to check
     * @param n - which position in the NGram
     * @return true if word is the nth word in the NGram
     */
    public boolean isNthWord(String word, int n){
        if (n > size() || n <= 0)
            return false;

        return words[n - 1].equalsIgnoreCase(word);
    }

    public String getNthWord(int n){
        return words[n - 1];
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

            for (int i = 0; i < Math.min(other.size(), this.size()); i++){
                int val = this.words[i].compareTo(other.words[i]);

                if (val != 0)
                    return val;
            }

            return 0;
        }

        return 1;
    }
}
