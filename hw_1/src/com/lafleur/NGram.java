package com.lafleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a single N-gram.
 */
public class NGram implements Comparable {

    public static final String PHI = ""; // PHI is the empty string - counts as the beginning of the sentence
    public static final NGram PHI_NGRAM = new NGram(new String[] { PHI });

    private String[] words;

    public NGram(String[] _words) {
        for(int i = 0; i < _words.length; i++)
            _words[i] = _words[i].toLowerCase();
        words = _words;
    }

    /**
     * Returns true if the first word of this NGram is the word provided.
     *
     * @param word - String word to check
     * @return true if word is the first word in this NGram
     */
    public boolean startsWith(String word) {
        return words[0].equalsIgnoreCase(word);
    }

    /**
     * Returns true if word is the nth word in this NGram.
     *
     * @param word - Word to check
     * @param n - which position in the NGram
     * @return true if word is the nth word in the NGram
     */
    public boolean isNthWord(String word, int n) {
        return !(n > size() || n <= 0) && words[n - 1].equalsIgnoreCase(word);
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

    public List<NGram> getUnigrams() {
        ArrayList<NGram> list = new ArrayList<>();

        for (int i = 0; i < size(); i++) {
            list.add(new NGram(new String[] { words[i] }));
        }

        return list;
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
        String[] words = sentence.split("\\s+");

        if (n == 1){
            for (String word : words) {
                if (word.equalsIgnoreCase(PHI))
                    continue;

                String[] unigram = new String[1];
                unigram[0] = word;
                nGrams.add(new NGram(unigram));
            }
        } else if (n == 2) {
            for (int i = 0; i < words.length; i++) {
                if (words[i].equals(PHI))
                    continue;

                String[] bigram = new String[2];

                if (i == 0)
                    bigram[0] = PHI;
                else
                    bigram[0] = words[i - 1];

                bigram[1] = words[i];

                nGrams.add(new NGram(bigram));
            }
        } else
            throw new RuntimeException("3+ grams have not been implemented!");

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
