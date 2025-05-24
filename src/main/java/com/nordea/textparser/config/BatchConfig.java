package com.nordea.textparser.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.nordea.textparser.model.Sentence;
import com.nordea.textparser.processor.SentenceProcessor;
import com.nordea.textparser.writer.CustomFileWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Value("${inputFilePath}")
    private String TEXT_PATH;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors()); 
        executor.setMaxPoolSize(10); 
        executor.setQueueCapacity(50); 
        executor.setThreadNamePrefix("BatchThread-");
        executor.initialize();
        return executor;
    }
    
    @Bean
    public Step processStep(StepBuilderFactory stepBuilderFactory, TaskExecutor taskExecutor
    		, SentenceProcessor processor, CustomFileWriter csvWriter) {
        return stepBuilderFactory.get("step")
                .<String, List<Sentence>>chunk(100)
                .reader(reader())
                .processor(processor)
                .writer(fileWriterBean())
                .taskExecutor(taskExecutor)
                .build();
    }
    
    @Bean
    public ItemWriter<List<Sentence>> fileWriterBean() {
        return new CustomFileWriter();
    }


    @Bean
    public Job processJob(JobBuilderFactory jobBuilderFactory, Step processStep,CsvHeaderJobListener csvHeaderJobListener, XmlMetaDataJobListener xmlFooterJobListener) {
        return jobBuilderFactory.get("job")
        		.listener(csvHeaderJobListener)
        		.listener(xmlFooterJobListener)
                .flow(processStep)
                .end()
                .build();
    }
    
    @Bean
    public FlatFileItemReader<String> reader() {
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(TEXT_PATH));
        reader.setLineMapper(new PassThroughLineMapper());        
        return reader;
    }

    
}

