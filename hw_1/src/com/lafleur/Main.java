package com.lafleur;

import java.io.File;
import java.util.List;

public class Main {

    public static void main (String[] args){
//        if (!acceptableArgs(args)) System.exit(1); // TODO: Uncomment this when you're ready to turn it in
        String[] sentences = {"This is a sentence", "This is another sentence", "And yet another sentence", "But that is all we have" };

        NGramModel unigramModel = new NGramModel(1, false);
        NGramModel bigramModel = new NGramModel(2, false);
        NGramModel smoothedModel = new NGramModel(2, true);

        for (String s : sentences) {
            unigramModel.addAll(NGram.getNGramsFromSentence(s, unigramModel.getValueOfN()));
            bigramModel.addAll(NGram.getNGramsFromSentence(s, bigramModel.getValueOfN()));
            smoothedModel.addAll(NGram.getNGramsFromSentence(s, smoothedModel.getValueOfN()));
        }

        System.out.println(unigramModel.trueProbability(unigramModel.probabilityOfSentence("And now this")));
        System.out.println(bigramModel.trueProbability(bigramModel.probabilityOfSentence("And now this")));
        System.out.println(smoothedModel.trueProbability(smoothedModel.probabilityOfSentence("And now this")));
    }

    private static List<String> getSentences(File f){

        return null; // TODO: This
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
