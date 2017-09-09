package com.lafleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main (String[] args){
        if (!acceptableArgs(args)) System.exit(1); // TODO: Uncomment this when you're ready to turn it in

        // The models we are going to be using
        NGramModel[] models = { new NGramModel(1, false), new NGramModel(2, false), new NGramModel(2, true) };

        // Train all the models from the training file
        List<String> sentences = getSentences(new File(args[0]));

        for(String s : sentences){
            for (NGramModel m : models)
                m.addAll(NGram.getNGramsFromSentence(s, m.getValueOfN()));
        }

        if (args[1].equalsIgnoreCase("-test")) {
            // Publish the log probs of each of the test sentences
            sentences = getSentences(new File(args[2]));

            for (String s : sentences) {
                System.out.println("S = " + s + "\n");
                System.out.println("Unsmoothed Unigrams, logprob(S) = " + models[0].probabilityOfSentence(s));
                System.out.println("Unsmoothed Bigrams, logprob(S) = " + models[1].probabilityOfSentence(s));
                System.out.println("Smoothed Bigrams, logprob(S) = " + models[2].probabilityOfSentence(s));
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

        if (!(args[1].equalsIgnoreCase("-test") || args[2].equalsIgnoreCase("-gen"))){
            System.err.println("\"" + args[2] + "\" is not an acceptable option!");
            return false;
        }

        if (!(new File(args[0])).exists() && (new File(args[2])).exists()){
            System.err.println("One of the provided files could not be found!");
            return false;
        }

        return true;
    }
}
