package com.nordea.textparser.processor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.nordea.textparser.model.Sentence;

@Component
public class SentenceProcessor implements ItemProcessor<String, List<Sentence>> {
	@Override
	public List<Sentence> process(String sentenceText) {
		
		synchronized (sentenceText) {
			List<Sentence> sentenceList = Arrays.stream(sentenceText.split("(?<=[.!?])")) // Splitting sentences properly
					.filter(str -> StringUtils.hasText(str)).map(Sentence::new).collect(Collectors.toList());
			 
			 return sentenceList;

		}
	}
}
