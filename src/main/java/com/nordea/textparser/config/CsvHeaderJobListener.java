package com.nordea.textparser.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CsvHeaderJobListener implements JobExecutionListener {

    @Autowired
    private MaxWordCounter maxWordCounter;

	@Value("${csvTempFilePath}")
    private String tempFilePath;
	
	@Value("${csvOutputFilePath}")
    private String finalFilePath;
	
	private static final Logger logger = LoggerFactory.getLogger(CsvHeaderJobListener.class);
		
    @Override
    public void afterJob(JobExecution jobExecution) {
        int maxWords = maxWordCounter.getMax();
    	
        String header = "Sentence," + IntStream.rangeClosed(1, maxWords)
                .mapToObj(i -> "Word" + i)
                .collect(Collectors.joining(","));

        try (
            BufferedReader reader = new BufferedReader(new FileReader(new File(tempFilePath)));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(finalFilePath)))
        ) {
            writer.write(header);
            writer.newLine();

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        	logger.info("Final CSV file has been created.");

        } catch (IOException e) {
        	logger.error("Failed to write header after job", e);
        }

    }

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		
	}
}
