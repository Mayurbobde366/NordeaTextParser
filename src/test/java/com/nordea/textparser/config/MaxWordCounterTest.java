package com.nordea.textparser.config;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class MaxWordCounterTest {

    private MaxWordCounter maxWordCounter;

    @Before
    public void setUp() {
        maxWordCounter = new MaxWordCounter();
    }

    @Test
    public void testInitialValues() {
        assertEquals("Initial max word count should be 0", 0, maxWordCounter.getMax());
        assertEquals("Initial sentence count should be 0", 0, maxWordCounter.getCSVSentenceCount().get());
    }

    @Test
    public void testUpdateMaxWordCount() {
        maxWordCounter.updateMax(5);
        assertEquals("Max word count should be updated to 5", 5, maxWordCounter.getMax());

        maxWordCounter.updateMax(3); // Lower value shouldn't override
        assertEquals("Max word count should still be 5", 5, maxWordCounter.getMax());

        maxWordCounter.updateMax(7); // Higher value should override
        assertEquals("Max word count should be updated to 7", 7, maxWordCounter.getMax());
    }

    @Test
    public void testUpdateCSVSentenceCount() {
        maxWordCounter.updateCSVSentenceCount(10);
        assertEquals("Sentence count should be updated to 10", 10, maxWordCounter.getCSVSentenceCount().get());

        maxWordCounter.updateCSVSentenceCount(8); // Lower value shouldn't override
        assertEquals("Sentence count should still be 10", 10, maxWordCounter.getCSVSentenceCount().get());

        maxWordCounter.updateCSVSentenceCount(15); // Higher value should override
        assertEquals("Sentence count should be updated to 15", 15, maxWordCounter.getCSVSentenceCount().get());
    }
}