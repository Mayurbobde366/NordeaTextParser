package com.nordea.textparser.config;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class MaxWordCounter {
    private final AtomicInteger maxWordCount = new AtomicInteger(0);
    private final AtomicInteger csvSentenceCount = new AtomicInteger(0);

    public void updateMax(int size) {
        maxWordCount.updateAndGet(prev -> Math.max(prev, size));
    }

    public int getMax() {
        return maxWordCount.get();
    }
    
    public void updateCSVSentenceCount(int size) {
    	csvSentenceCount.updateAndGet(prev -> Math.max(prev, size));
    }

    public AtomicInteger getCSVSentenceCount() {
        return csvSentenceCount;
    }

}
