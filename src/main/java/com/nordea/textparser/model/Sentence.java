package com.nordea.textparser.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

public class Sentence {
	private final List<String> words;

	public Sentence(String text) {
		this.words = Arrays.stream(text.replaceAll("[.!?,]", "").split("\\s+")) // Extract individual words
				.filter(str -> StringUtils.hasText(str))
				.sorted((s1,s2) -> s1.compareToIgnoreCase(s2)) // Sort words alphabetically
				.collect(Collectors.toList());
	}

	public List<String> getWords() {
		return words;
	}

}
