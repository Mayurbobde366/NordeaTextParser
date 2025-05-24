package com.nordea.textparser.config;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.JobExecution;

@RunWith(MockitoJUnitRunner.class)
public class CsvHeaderJobListenerTest {

    @Mock
    private MaxWordCounter maxWordCounter;

    private CsvHeaderJobListener listener;
    
    private static final String TEMP_FILE_PATH = "temp.csv";
    private static final String FINAL_FILE_PATH = "final.csv";

    @Before
    public void setUp() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        listener = new CsvHeaderJobListener();
        Mockito.when(maxWordCounter.getMax()).thenReturn(2);
        Field field = CsvHeaderJobListener.class.getDeclaredField("maxWordCounter");
        field.setAccessible(true);
        field.set(listener, maxWordCounter);
        
        // Set tempFilePath
        Field tempFilePathField = CsvHeaderJobListener.class.getDeclaredField("tempFilePath");
        tempFilePathField.setAccessible(true);
        tempFilePathField.set(listener, TEMP_FILE_PATH);

        // Set finalFilePath
        Field finalFilePathField = CsvHeaderJobListener.class.getDeclaredField("finalFilePath");
        finalFilePathField.setAccessible(true);
        finalFilePathField.set(listener, FINAL_FILE_PATH);


        // Simulate a temporary file with some data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE_PATH))) {
            writer.write("Sample sentence,word1,word2");
        }

    }

    @Test
    public void testAfterJobWritesHeaderAndCopiesData() throws IOException {
        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        listener.afterJob(jobExecution);

        List<String> lines = Files.readAllLines(Paths.get(FINAL_FILE_PATH));
        // Debug prints
        System.out.println("Expected: Sentence,Word1,Word2");
        System.out.println("Actual: " + lines.get(0));


        assertEquals("Sentence,Word1,Word2", lines.get(0)); // Validating header
        assertEquals("Sample sentence,word1,word2", lines.get(1)); // Validating copied content
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEMP_FILE_PATH));
        Files.deleteIfExists(Paths.get(FINAL_FILE_PATH));
    }
}

