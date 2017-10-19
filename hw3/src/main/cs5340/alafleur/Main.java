package main.cs5340.alafleur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
        ArrayList<FeatureType> desiredFeatures = new ArrayList<>();

        for (int i = 3; i < args.length; i++) {
            FeatureType f = FeatureType.fromString(args[i]);

            if (f == null)
                throw new RuntimeException("I don't know what \"" + args[i] + "\" is.");

            desiredFeatures.add(f);
        }

//        System.out.println("Desired features are " + desiredFeatures);

        // Output the proper files
        outputFiles(trainFile, desiredFeatures);
        outputFiles(testFile, desiredFeatures);
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
    }

    /**
     * Prints out a readable file on the file passed.
     *
     * @param f - File to look at and convert to readable format
     * @param features - Features to consider
     */
    private static void outputReadableFile(File f, ArrayList<FeatureType> features, ArrayList<Word> words) {
        // TODO: Figure out if we are being used in test file or training file and print accordingly
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

    private static ArrayList<Word> getWords(File f, ArrayList<FeatureType> features) {
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

        // We want to make sure we only have 1 __END__ at the end of the file, rather than two or twenty
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

            Word w;

            if (i == 0 || omegaAppeared) {
                nextLineSplit = lines.get(i + 1).split(REGEX);

                if (nextLineSplit[0].equals(END_OF_SENTENCE)) {
                    w = new Word(Word.PHI, thisLineSplit[2], Word.OMEGA, Word.PHI_POS,
                            thisLineSplit[1], Word.OMEGA_POS, thisLineSplit[0]);
                } else {
                    w = new Word(Word.PHI, thisLineSplit[2], nextLineSplit[2], Word.PHI_POS,
                            thisLineSplit[1], nextLineSplit[1], thisLineSplit[0]);
                    omegaAppeared = false;
                }
            } else if (thisLineSplit[0].equals(END_OF_SENTENCE)) {
                omegaAppeared = true;
                continue;
            } else {
                previousLineSplit = lines.get(i - 1).split(REGEX);
                nextLineSplit = lines.get(i + 1).split(REGEX);

                if (nextLineSplit[0].equals(END_OF_SENTENCE)) {
                    w = new Word(previousLineSplit[2], thisLineSplit[2], Word.OMEGA, previousLineSplit[1],
                            thisLineSplit[1], Word.OMEGA_POS, thisLineSplit[0]);
                } else {
                    w = new Word(previousLineSplit[2], thisLineSplit[2], nextLineSplit[2], previousLineSplit[1],
                            thisLineSplit[1], nextLineSplit[1], thisLineSplit[0]);
                }
            }

            words.add(w);
        }

        return words;
    }
}
