package com.nordea.textparser.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Value;

@RunWith(MockitoJUnitRunner.class)
public class XmlMetaDataJobListenerTest {

    @Value("${xmlOutputFilePath}")
    private String XML_PATH = "test_output.xml";

    private XmlMetaDataJobListener listener;

    @Before
    public void setUp() throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        listener = new XmlMetaDataJobListener();
        
        // Use reflection to inject file path (if private)
            Field xmlPathField = XmlMetaDataJobListenerTest.class.getDeclaredField("XML_PATH");
            xmlPathField.setAccessible(true);
            xmlPathField.set(listener, XML_PATH);

        // Ensure the file is clean before tests
        Files.deleteIfExists(Paths.get(XML_PATH));
    }

    @Test
    public void testBeforeJobWritesMetadata() throws IOException {
        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        listener.beforeJob(jobExecution);

        List<String> lines = Files.readAllLines(Paths.get(XML_PATH));

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone = \"yes\"?>", lines.get(0)); // Metadata
        assertEquals("<text>", lines.get(1)); // Opening tag
    }

    @Test
    public void testAfterJobWritesFooter() throws IOException {
        JobExecution jobExecution = Mockito.mock(JobExecution.class);
        listener.beforeJob(jobExecution); // Create metadata first
        listener.afterJob(jobExecution); // Append footer

        List<String> lines = Files.readAllLines(Paths.get(XML_PATH));

        assertEquals("</text>", lines.get(lines.size() - 1)); // Footer should be last line
    }

    @Test
    public void testHandlesFileWriteExceptionGracefully() {
        try {
            java.lang.reflect.Field xmlPathField = XmlMetaDataJobListener.class.getDeclaredField("XML_PATH");
            xmlPathField.setAccessible(true);
            xmlPathField.set(listener, "/invalid_path/test_output.xml"); // Force failure

            JobExecution jobExecution = Mockito.mock(JobExecution.class);
            listener.beforeJob(jobExecution); // Should fail

            fail("Expected IOException but the test continued");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Failed to write"));
        }
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(XML_PATH));
    }
}
