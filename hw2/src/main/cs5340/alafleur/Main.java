package main.cs5340.alafleur;

import java.io.File;

/**
 * Main class. Runs the experiments.
 */
public class Main {


    public static void main(String[] args) {
        if (!acceptableArgs(args)) System.exit(1);


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
