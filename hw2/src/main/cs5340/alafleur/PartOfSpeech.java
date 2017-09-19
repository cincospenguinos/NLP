package cs5340.u0669715;

/**
 * A single part of speech in a sentence.
 */
public enum PartOfSpeech {

    NOUN, VERB, INF, PREP;



    @Override
    public String toString() { // TODO: Add these as they show up
        switch(this) {
            case NOUN:
                return "NOUN";
            case VERB:
                return "VERB";
            case INF:
                return "INF";
            case PREP:
                return "PREP";
        }

        return super.toString();
    }
}
