package com.nordea.textparser.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.nordea.textparser.model.Sentence;

public class SentenceProcessorTest {

    private final SentenceProcessor processor = new SentenceProcessor();

    @Test
    public void testSentenceProcessing() throws Exception {
    	List<Sentence> result = processor.process("Peter called for the wolf!");

        assertNotNull(result); //Ensures object creation
        assertEquals(5, result.get(0).getWords().size()); // Validates word splitting
    }
}

