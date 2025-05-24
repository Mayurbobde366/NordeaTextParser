package com.nordea.textparser.writer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.nordea.textparser.config.MaxWordCounter;
import com.nordea.textparser.model.Sentence;

public class CustomFileWriterTest {

    private CustomFileWriter customFileWriter;
    private MaxWordCounter maxWordCounter;
    private String CSV_PATH = "output.csv";
    private String XML_PATH = "output.xml";
    
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    
    @Before
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        mock(BufferedWriter.class);
        customFileWriter = new CustomFileWriter();
        maxWordCounter = Mockito.mock(MaxWordCounter.class);

        Field field = CustomFileWriter.class.getDeclaredField("maxWordCounter");
        field.setAccessible(true);
        field.set(customFileWriter, maxWordCounter);
        Mockito.when(maxWordCounter.getMax()).thenReturn(2);
        
        // Set csvFilePath
        Field tempFilePathField = CustomFileWriter.class.getDeclaredField("CSV_PATH");
        tempFilePathField.setAccessible(true);
        tempFilePathField.set(customFileWriter, CSV_PATH);

        // Set xmlFilePath
        Field finalFilePathField = CustomFileWriter.class.getDeclaredField("XML_PATH");
        finalFilePathField.setAccessible(true);
        finalFilePathField.set(customFileWriter, XML_PATH);


    }
    
    @Test
    public void testWriteCsvFile() throws Exception {
        List<Sentence> sentences = List.of(new Sentence("Mary had a little lamb."), new Sentence("Peter called for the wolf!"));
        Mockito.when(maxWordCounter.getCSVSentenceCount()).thenReturn(atomicInteger);
        customFileWriter.writeCsvFile(sentences, 3);
        assertTrue(new java.io.File(CSV_PATH).exists());
    }
    
    @Test
    public void testWriteXmlFile() throws Exception {
        List<Sentence> sentences = List.of(new Sentence("Mary had a little lamb."), new Sentence("Peter called for the wolf!"));

        customFileWriter.writeXmlFile(sentences);

        assertTrue(new java.io.File(XML_PATH).exists());
    }

    @Test
    public void testFormatCsvRow() {
        Sentence sentence = new Sentence("Mary had a little lamb");
        String result = customFileWriter.formatCsvRow(sentence, 3, 1);

        assertEquals("Sentence 1,a,had,lamb,little,Mary", result);
    }

    @Test
    public void testWriteMethod() throws Exception {
        List<List<Sentence>> sentencesBatch = List.of(
            List.of(new Sentence("Mary had a little lamb"), new Sentence("Peter called for the wolf"))
        );
        Mockito.when(maxWordCounter.getCSVSentenceCount()).thenReturn(atomicInteger);
        customFileWriter.write(sentencesBatch);

        assertTrue(new java.io.File(CSV_PATH).exists());
        assertTrue(new java.io.File(XML_PATH).exists());
    }
    
    @Test(expected = RuntimeException.class)
    public void testWriteCsvFile_ExceptionHandling() throws Exception {
        customFileWriter.writeCsvFile(null, 2);
    }


}
