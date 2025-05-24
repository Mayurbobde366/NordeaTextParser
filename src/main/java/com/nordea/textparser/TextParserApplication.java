package com.nordea.textparser;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nordea.textparser.config.XmlMetaDataJobListener;

@SpringBootApplication
public class TextParserApplication implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(XmlMetaDataJobListener.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;
    
	@Value("${csvTempFilePath}")
    private String tempFilePath;

    public static void main(String[] args) {
        SpringApplication.run(TextParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobExecution execution = jobLauncher.run(job, new JobParameters());
        logger.info("Job Status: " + execution.getStatus());
        File tempFile = new File(tempFilePath);
        tempFile.deleteOnExit();
        System.exit(0); // Exit after execution
    }
}

