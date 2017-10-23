package main.cs5340.alafleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    private static TreeSet<String> trainingWords;
    private static TreeSet<String> trainingPos;

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Expecting 4+ arguments");
            System.exit(1);
        }

        trainingWords = new TreeSet<>();
        trainingPos = new TreeSet<>();

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
        ArrayList<FeatureType> desiredFeatures = new ArrayList<>();

        for (int i = 3; i < args.length; i++) {
            FeatureType f = FeatureType.fromString(args[i]);

            if (f == null)
                throw new RuntimeException("I don't know what \"" + args[i] + "\" is.");

            desiredFeatures.add(f);
        }

        // Output the proper files
        outputFiles(trainFile, desiredFeatures, true);
        outputFiles(testFile, desiredFeatures, false);
    }

    /**
     * Outputs readable and vector files. This method is here out of convenience.
     *
     * @param f - File to look at and convert to readable format
     * @param features - Features to consider
     */
    private static void outputFiles(File f, ArrayList<FeatureType> features, boolean isTraining) {
        ArrayList<Word> words = getWords(f, isTraining);
        outputReadableFile(f, features, words);
        outputVectorFile(f, features, words);
    }

    /**
     * Prints out a readable file on the file passed.
     *
     * @param f - File to look at and convert to readable format
     * @param features - Features to consider
     */
    private static void outputReadableFile(File f, ArrayList<FeatureType> features, ArrayList<Word> words) {
        try {
            PrintWriter out = new PrintWriter(f.getName() + ".readable");

            for (Word w : words) {
                for (FeatureType fType : FeatureType.values()) {
                    if (features.contains(fType)) {
                        out.println(fType + ": " + w.getFeatureString(fType));
                    } else {
                        out.println(fType + ": n/a" );
                    }
                }

                out.println();
            }

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
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

    private static ArrayList<Word> getWords(File f, boolean isTraining) {
        ArrayList<String> lines = new ArrayList<>();
        final String END_OF_SENTENCE = "\\\\END\\\\";
        final String REGEX = "\\s+";

        try {
            Scanner s = new Scanner(f);
            while(s.hasNextLine()) {
                String str = s.nextLine();

                if (!str.isEmpty())
                    lines.add(str.trim());
                else
                    lines.add(END_OF_SENTENCE);
            }

            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // We want to make sure we only have 1 __END__ for every new sentence
        for (int i = lines.size() - 2; i >= 0; i--) {
            if (lines.get(i).equals(END_OF_SENTENCE) && lines.get(i + 1).equals(END_OF_SENTENCE)) {
                lines.remove(i + 1);
            }
        }

        ArrayList<Word> words = new ArrayList<>();
        boolean omegaAppeared = false;

        for(int i = 0; i < lines.size() - 1; i++) {
            String[] previousLineSplit;
            String[] thisLineSplit = lines.get(i).split(REGEX);
            String[] nextLineSplit;

            String previousWord;
            String thisWord;
            String nextWord;

            String previousPos;
            String thisPos;
            String nextPos;

            String label = thisLineSplit[0];

            if (i == 0 || omegaAppeared) {
                thisWord = thisLineSplit[2];
                thisPos = thisLineSplit[1];

                nextLineSplit = lines.get(i + 1).split(REGEX);

                previousWord = Word.PHI;
                previousPos = Word.PHI_POS;

                if (nextLineSplit[0].equals(END_OF_SENTENCE)) {
                    nextWord = Word.OMEGA;
                    nextPos = Word.OMEGA_POS;
                } else {
                    nextWord = nextLineSplit[2];
                    nextPos = nextLineSplit[1];

                    omegaAppeared = false;
                }
            } else if (thisLineSplit[0].equals(END_OF_SENTENCE)) {
                omegaAppeared = true;
                continue;
            } else {
                thisWord = thisLineSplit[2];
                thisPos = thisLineSplit[1];

                previousLineSplit = lines.get(i - 1).split(REGEX);
                nextLineSplit = lines.get(i + 1).split(REGEX);

                previousWord = previousLineSplit[2];
                previousPos = previousLineSplit[1];

                if (nextLineSplit[0].equals(END_OF_SENTENCE)) {
                    nextWord = Word.OMEGA;
                    nextPos = Word.OMEGA_POS;
                } else {
                    nextWord = nextLineSplit[2];
                    nextPos = nextLineSplit[1];
                }
            }

            // When we print the test file, we need to make sure that all of the words/pos are listed as UNK
            // if they weren't in the training file

            // TODO: Should we worry about thisWord/thisPos?
            if (!isTraining) {
                if (!trainingWords.contains(previousWord))
                    previousWord = Word.UNK_WORD;
                if (!trainingWords.contains(nextWord))
                    nextWord = Word.UNK_WORD;
                if (!trainingPos.contains(previousPos))
                    previousPos = Word.UNK_POS;
                if (!trainingPos.contains(nextPos))
                    nextPos = Word.UNK_POS;
            }

            Word w = new Word(previousWord, thisWord, nextWord, previousPos, thisPos, nextPos, label);
            words.add(w);
        }

        if (isTraining) {
            for (Word w : words) {
                trainingWords.add(w.getFeatureString(FeatureType.WORD));
                trainingPos.add(w.getFeatureString(FeatureType.POS));
            }
        }

        return words;
    }
}
