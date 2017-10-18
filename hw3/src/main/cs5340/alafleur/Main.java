package main.cs5340.alafleur;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Expecting 4+ arguments");
            System.exit(1);
        }

        File trainFile = new File(args[0]);
        File testFile = new File(args[1]);
        File locsFile = new File(args[2]);

        if (!(trainFile.exists() && testFile.exists() && locsFile.exists())) {
            System.err.println("One of the files could not be found");
            System.exit(1);
        }

        ArrayList<String> features = new ArrayList<>();

        for (int i = 3; i < args.length; i++)
            features.add(args[i]);

        System.out.println(features);
    }
}
