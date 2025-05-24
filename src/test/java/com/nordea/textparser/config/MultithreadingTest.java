package com.nordea.textparser.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MultithreadingTest {

    @Test
    public void testTaskExecutorConfiguration() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.initialize();

        assertEquals(5, executor.getCorePoolSize()); //Validates pool size
        assertEquals(10, executor.getMaxPoolSize());
    }
}

