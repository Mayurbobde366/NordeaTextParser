package com.nordea.textparser.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class SentenceTest {

    @Test
    public void testWordExtraction() {
        Sentence sentence = new Sentence("Mary had a little lamb.");
        List<String> words = sentence.getWords();

        assertEquals(5, words.size()); // Validates correct word count
        assertEquals("a", words.get(0)); // Ensures correct word order
        assertFalse(words.contains(".")); // Confirms punctuation removal
    }

//    @Test
//    public void testXmlConversion() {
//        Sentence sentence = new Sentence("Peter called for the wolf.");
//        String xmlOutput = sentence.toXML();
//
//        assertTrue(xmlOutput.contains("<word>Peter</word>"));
//    }
}

