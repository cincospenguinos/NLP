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

        sentences = getSentences(new File(args[2]));
        models[2].smoothOver();

        DecimalFormat df = new DecimalFormat("###.####");

        for (String s : sentences) {
            System.out.println("S = " + s + "\n");

            double d0 = models[0].probabilityOfSentence(s);
            double d1 = models[1].probabilityOfSentence(s);
            double d2 = models[2].probabilityOfSentence(s);

            if (((Double)d0).isInfinite())
                System.out.println("Unsmoothed Unigrams, logprob(S) = undefined");
            else
                System.out.println("Unsmoothed Unigrams, logprob(S) = " + df.format(d0));

            if (((Double)d1).isInfinite())
                System.out.println("Unsmoothed Bigrams, logprob(S) = undefined");
            else
                System.out.println("Unsmoothed Bigrams, logprob(S) = " + df.format(d1));

            if (((Double)d2).isInfinite())
                System.out.println("Smoothed Bigrams, logprob(S) = undefined");
            else
                System.out.println("Smoothed Bigrams, logprob(S) = " + df.format(d2));

            System.out.println();
        }
    }

    private static List<String> getSentences(File f){
        ArrayList<String> sentences = new ArrayList<>();

        try {
            Scanner s = new Scanner(f);

            while (s.hasNextLine())
                sentences.add(s.nextLine());

            s.close();
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

        if (!(args[1].equalsIgnoreCase("-test"))){
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
