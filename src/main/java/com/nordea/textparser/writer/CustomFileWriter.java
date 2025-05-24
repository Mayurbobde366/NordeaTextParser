package com.nordea.textparser.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.nordea.textparser.config.MaxWordCounter;
import com.nordea.textparser.model.Sentence;

@Component
public class CustomFileWriter implements ItemWriter<List<Sentence>> {

	@Value("${csvTempFilePath}")
    private String CSV_PATH;
	
	@Value("${xmlOutputFilePath}")
    private String XML_PATH;
	
    @Autowired
    private MaxWordCounter maxWordCounter;

    @Override
    public void write(List<? extends List<Sentence>> sentencesBatch) throws Exception {
    	
        synchronized (sentencesBatch) {
            List<Sentence> sentences = sentencesBatch.stream().flatMap(List::stream).collect(Collectors.toList());

            // Determine max word count for dynamic headers
            int maxWords = sentences.stream()
                                    .mapToInt(sentence -> sentence.getWords().size())
                                    .max()
                                    .orElse(1);
        	maxWordCounter.updateMax(maxWords);


            // Write CSV and XML files
            writeCsvFile(sentences, maxWords);
            writeXmlFile(sentences);
        } 
        
    }

    protected void writeCsvFile(List<Sentence> sentences, int maxWords) throws Exception {
        synchronized (sentences) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_PATH,true))) {
            int row = maxWordCounter.getCSVSentenceCount().get();
            // Write all sentences dynamically
            for (Sentence sentence : sentences) {
            	row = maxWordCounter.getCSVSentenceCount().getAndIncrement();
            	String writerStr = formatCsvRow(sentence, maxWords, row);
                writer.write(writerStr);
                writer.write(System.lineSeparator());
                writer.flush();
                maxWordCounter.updateCSVSentenceCount(row);
            }
        }
       }
    }

    protected void writeXmlFile(List<Sentence> sentences) throws Exception {
        synchronized (sentences) {
           	try (BufferedWriter writer = new BufferedWriter(new FileWriter(XML_PATH,true))) {
                for (Sentence sentence : sentences) {
                    writer.write(toXML(sentence));
                    writer.write(System.lineSeparator());
                    writer.flush();
                }                
            }
		}
 
    }

    protected String formatCsvRow(Sentence sentence, int maxWords, int row) {
        List<String> words = sentence.getWords();
        while (words.size() < maxWordCounter.getMax()) words.add(""); // Pad missing columns
        return "Sentence " + row + "," + String.join(",", words);
    }
    
    protected String toXML(Sentence sentence) {
		StringBuilder xml = new StringBuilder("  <sentence>\n");
		List<String> words = sentence.getWords();
		for (String word : words) {
			if (StringUtils.hasText(word)) {
				//word.replaceAll("[^a-zA-Z0-9]", "");
				xml.append("    <word>").append(word).append("</word>\n"); // Correctly formats words
			}
		}
		xml.append("  </sentence>");
		return xml.toString();
	}
}
