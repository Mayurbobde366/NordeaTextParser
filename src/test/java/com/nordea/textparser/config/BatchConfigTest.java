package com.nordea.textparser.config;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.nordea.textparser.processor.SentenceProcessor;
import com.nordea.textparser.writer.CustomFileWriter;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class BatchConfigTest {

    @InjectMocks
    private BatchConfig batchConfig;

    @Mock
    private JobBuilderFactory jobBuilderFactory;

    @Mock
    private StepBuilderFactory stepBuilderFactory;

    @Mock
    private SentenceProcessor processor;

    @Mock
    private CustomFileWriter csvWriter;

    @Mock
    private TaskExecutor taskExecutor;

    @Mock
    private Step step;
    
    @Mock
    private JobRepository jobRepository;
    
    @Mock
    private CsvHeaderJobListener csvHeaderJobListener;
    
    @Mock
    private XmlMetaDataJobListener xmlFooterJobListener;

    @Mock
    private Job job;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(jobBuilderFactory.get(anyString())).thenReturn(new JobBuilder("testJob"));
        when(jobBuilderFactory.get(anyString())).thenReturn(new JobBuilder("testJob").repository(jobRepository));
        when(stepBuilderFactory.get(anyString())).thenReturn(new StepBuilder("testStep"));

    }

    
    @Test
    public void testTaskExecutor() {
        TaskExecutor executor = batchConfig.taskExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof ThreadPoolTaskExecutor);
    }
    
    @Test
    public void testProcessStep() {
        Step step = batchConfig.processStep(stepBuilderFactory, taskExecutor, processor, csvWriter);
        assertNotNull(step);
    }
    
    @Test
    public void testWriterBean() {
        ItemWriter<?> writer = batchConfig.fileWriterBean();
        assertNotNull(writer);
        assertTrue(writer instanceof CustomFileWriter);
    }
    @Test
    public void testProcessJob() {
        Job job = batchConfig.processJob(jobBuilderFactory, step, csvHeaderJobListener, xmlFooterJobListener);
        assertNotNull(job);
    }
//    @Test
//    public void testReaderBean() {
//        FlatFileItemReader<String> reader = batchConfig.reader();
//        assertNotNull(reader);
//        assertEquals("src/main/resources/sample.txt", ((FileSystemResource) reader.getResource()).getPath());
//    }


}
