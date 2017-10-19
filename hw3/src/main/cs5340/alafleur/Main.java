package main.cs5340.alafleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Expecting 4+ arguments");
            System.exit(1);
        }

        File trainFile = new File(args[0]);
        File testFile = new File(args[1]);
        File locsFile = new File(args[2]);

        // Gather up all the locations
        TreeSet<String> locations = new TreeSet<>();

        try {
            Scanner scanner = new Scanner(locsFile);
            while(scanner.hasNextLine())
                locations.add(scanner.nextLine().trim());
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Word.setLocations(locations);

        // Gather up all the desired features this time
        ArrayList<FeatureType> featureTypes = new ArrayList<>();

        for (int i = 3; i < args.length; i++) {
            FeatureType f = FeatureType.fromString(args[i]);

            if (f == null)
                throw new RuntimeException("I don't know what \"" + args[i] + "\" is.");

            featureTypes.add(f);
        }

        // Output the proper files
        outputFiles(trainFile, featureTypes);
        outputFiles(testFile, featureTypes);
    }

    /**
     * Outputs readable and vector files. This method is here out of convenience.
     *
     * @param f - File to look at and convert to readable format
     * @param features - Features to consider
     */
    private static void outputFiles(File f, ArrayList<FeatureType> features) {
        ArrayList<Word> words = getWords(f, features);
        outputReadableFile(f, features, words);
        outputVectorFile(f, features, words);

        System.out.println(words);
    }

    /**
     * Prints out a readable file on the file passed.
     *
     * @param f - File to look at and convert to readable format
     * @param features - Features to consider
     */
    private static void outputReadableFile(File f, ArrayList<FeatureType> features, ArrayList<Word> words) {
        // TODO: This
    }

    /**
     * Prints out a vector file on the file passed.
     *
     * @param f - File to look at and convert to readable format
     * @param features - Features to consider
     */
    private static void outputVectorFile(File f, ArrayList<FeatureType> features, ArrayList<Word> words) {
        // TODO: This
    }

    private static ArrayList<Word> getWords(File f, ArrayList<FeatureType> features) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            Scanner s = new Scanner(f);
            while(s.hasNextLine()) {
                String str = s.nextLine();

                if (!str.isEmpty())
                    lines.add(str.trim());
            }

            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<Word> words = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++) {
            String[] previousLineSplit;
            String[] thisLineSplit = lines.get(i).split("\\s+");
            String[] nextLineSplit;

            Word w;

            if (i == 0) {
                nextLineSplit = lines.get(i + 1).split("\\s+");

                w = new Word(Word.PHI, thisLineSplit[2], nextLineSplit[2], Word.PHI_POS,
                        thisLineSplit[1], nextLineSplit[1], thisLineSplit[0]);
            } else if (i == lines.size() - 1) {
                previousLineSplit = lines.get(i - 1).split("\\s+");
                w = new Word(previousLineSplit[2], thisLineSplit[2], Word.OMEGA, previousLineSplit[1],
                        thisLineSplit[1], Word.OMEGA_POS, thisLineSplit[0]);
            } else {
                previousLineSplit = lines.get(i - 1).split("\\s+");
                nextLineSplit = lines.get(i + 1).split("\\s+");
                w = new Word(previousLineSplit[2], thisLineSplit[2], nextLineSplit[2], previousLineSplit[1],
                        thisLineSplit[1], nextLineSplit[2], thisLineSplit[0]);
            }

            words.add(w);
        }

        return words;
    }
}
