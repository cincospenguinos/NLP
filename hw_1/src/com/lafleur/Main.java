package com.lafleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    public static void main (String[] args){
        if (!acceptableArgs(args)) System.exit(1);

        // The models we are going to be using
        NGramModel[] models = { new NGramModel(1, false), new NGramModel(2, false), new NGramModel(2, true) };

        // Train all the models from the training file
        List<String> sentences = getSentences(new File(args[0]));

        for(String s : sentences){
            for (NGramModel m : models)
                m.addAll(NGram.getNGramsFromSentence(s, m.getValueOfN()));
        }

        if (args[1].equalsIgnoreCase("-test")) {
            sentences = getSentences(new File(args[2]));

            // Gather all of the new NGrams from the test file and smooth over the smooth model with them
            TreeSet<NGram> newNGrams = new TreeSet<>();

            for (String s : sentences)
                newNGrams.addAll(NGram.getNGramsFromSentence(s, 2));

            models[2].smoothOver(newNGrams);

            DecimalFormat df = new DecimalFormat("###.####");

            for (String s : sentences) {
                System.out.println("S = " + s + "\n");
                System.out.println("Unsmoothed Unigrams, logprob(S) = " + df.format(models[0].probabilityOfSentence(s)));
                System.out.println("Unsmoothed Bigrams, logprob(S) = " + df.format(models[1].probabilityOfSentence(s)));
                System.out.println("Smoothed Bigrams, logprob(S) = " + df.format(models[2].probabilityOfSentence(s)));
                System.out.println();
            }
        } else {
            // TODO: Generate sentences
        }
    }

    private static List<String> getSentences(File f){
        ArrayList<String> sentences = new ArrayList<>();

        try {
            Scanner s = new Scanner(f);

            while (s.hasNextLine())
                sentences.add(s.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return sentences;
    }

    private static boolean acceptableArgs(String[] args){
        if (args.length != 3) {
            System.err.println("Expected 3 arguments; received " + args.length);
            return false;
        }

        if (!(args[1].equalsIgnoreCase("-test") || args[1].equalsIgnoreCase("-gen"))){
            System.err.println("\"" + args[1] + "\" is not an acceptable option!");
            return false;
        }

        if (!(new File(args[0])).exists() && (new File(args[2])).exists()){
            System.err.println("One of the provided files could not be found!");
            return false;
        }

        return true;
    }
}
