package main.cs5340.alafleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class. Runs the experiments.
 */
public class Main {


    public static void main(String[] args) {
        if (!acceptableArgs(args)) System.exit(1);

        // Gather all of the probabilities
        ViterbiProbabilitiesManager manager = new ViterbiProbabilitiesManager();

        try {
            Scanner scanner = new Scanner(new File(args[0]));

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("\\s+");

                if (line.length != 3)
                    break;

                double probability = Double.parseDouble(line[2]);

                // If the first word is a part of speech, then it's transmission
                if (PartOfSpeech.isPartOfSpeech(line[0])) {
                    manager.addTransmissionProbability(PartOfSpeech.toPartOfSpeech(line[0]),
                            PartOfSpeech.toPartOfSpeech(line[1]), probability);
                } else { // Otherwise, it's emission
                    manager.addEmissionProbability(line[0], PartOfSpeech.toPartOfSpeech(line[1]), probability);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Viterbi viterbi = new Viterbi(manager);

        try {
            Scanner scanner = new Scanner(new File(args[1]));

            while (scanner.hasNextLine()) {

            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static boolean acceptableArgs(String[] args) {
        if (args.length != 2) {
            System.err.println("Expected two arguments; got " + args.length);
            return false;
        }

        File f1 = new File(args[0]);
        File f2 = new File(args[1]);

        if (!f1.exists() || !f2.exists()) {
            System.err.println("Could not find one of the files!");
            return false;
        }

        return true;
    }
}
