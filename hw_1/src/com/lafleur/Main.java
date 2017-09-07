package com.lafleur;

import java.io.File;

public class Main {

    public static void main (String[] args){
//        if (!acceptableArgs(args)) System.exit(1); // TODO: Uncomment this when you're ready to turn it in
        String sentence = "This is a sentence";

        for(NGram ngram : NGram.getNGramsFromSentence(sentence, 1))
            System.out.println(ngram.toString());
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
