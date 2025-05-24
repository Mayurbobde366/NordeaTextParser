package com.nordea.textparser.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class XmlMetaDataJobListener implements JobExecutionListener {
	
	@Value("${xmlOutputFilePath}")
    private String XML_PATH;
	private static final Logger logger = LoggerFactory.getLogger(XmlMetaDataJobListener.class);

		
    @Override
    public void afterJob(JobExecution jobExecution) {
    	
       	try (BufferedWriter writer = new BufferedWriter(new FileWriter(XML_PATH,true))) {            
            writer.write("</text>");
        	logger.info("xml file mata footer data has been created");
        } catch (IOException e) {
			// TODO Auto-generated catch block
        	logger.error("Failed to write footer in XML file", e);
		}
    }

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
       	try (BufferedWriter writer = new BufferedWriter(new FileWriter(XML_PATH,true))) {            
       		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone = \"yes\"?>\n<text>\n");
            writer.flush();
        	logger.info("xml file mata data header has been created");

        } catch (IOException e) {
			// TODO Auto-generated catch block
        	logger.error("Failed to write header in XML file", e);
		}
	}
}
