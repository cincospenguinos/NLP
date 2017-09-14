package com.lafleur;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

/**
 * Test class for NGram class.
 */
public class NGramTest {

    @org.junit.Test
    public void testComparison() {
        String[] set1 = {"Hello", "there"};
        String[] set2 = {"Hello", "there"};

        NGram one = new NGram(set1);
        NGram two = new NGram(set2);

        assertTrue(one.compareTo(two) == 0);

        two = new NGram(set1);

        assertTrue(one.compareTo(two) == 0);

        String sentence = "This is a sentence !";

        List<NGram> list1 = NGram.getNGramsFromSentence(sentence, 1);
        List<NGram> list2 = NGram.getNGramsFromSentence(sentence, 1);

        for (int i = 0; i < list1.size(); i++)
            assertTrue(list1.get(i).compareTo(list2.get(i)) == 0);

        NGramModel model = new NGramModel(1, false);
        model.addAll(list1);

        assertTrue(model.vocabSize() == 5);
        assertTrue(model.totalFrequency() == 5);

        model = new NGramModel(1, false);

        String[] corpora = { "Hello hello HeLlO", "Hello hello", "hellO heLLo", "hElLo HELLO" };

        for (String s : corpora)
            model.addAll(NGram.getNGramsFromSentence(s, 1));

        assertTrue(model.totalFrequency() == 9);
        assertTrue(model.vocabSize() == 1);
        assertTrue(model.probabilityOfSentence("hELLo") == 0.0);
    }
}
