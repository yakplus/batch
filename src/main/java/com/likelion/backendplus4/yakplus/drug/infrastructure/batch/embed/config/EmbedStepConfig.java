package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.config;

import java.util.Map;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.reader.DrugIdRangePartitioner;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.switcher.application.port.out.EmbeddingSwitchPort;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class EmbedStepConfig {
	private final String READER_NAME = "rawDataReader";
	private final int PAGE_SIZE = 50;
	private final DrugIdRangePartitioner drugIdRangePartitioner;
	private final TaskExecutor taskExecutor;

	public EmbedStepConfig(DrugIdRangePartitioner drugIdRangePartitioner,
							@Qualifier("batchExecutorManyThread")
	                       TaskExecutor taskExecutor){
		this.drugIdRangePartitioner = drugIdRangePartitioner;
		this.taskExecutor = taskExecutor;
	}

	@Bean
	public Step switchModelStepToGpt(JobRepository jobRepository,
		PlatformTransactionManager tx,
		EmbeddingSwitchPort switchPort) {

		Tasklet tasklet = (contribution, chunkContext) -> {
			switchPort.switchTo("gptEmbeddingLoadingAdapter");
			return RepeatStatus.FINISHED;
		};

		return new StepBuilder("switchModelStepToGpt", jobRepository)
			.tasklet(tasklet, tx)
			.build();
	}

	@Bean
	public Step switchModelStepToKmbert(JobRepository jobRepository,
		PlatformTransactionManager tx,
		EmbeddingSwitchPort switchPort) {

		Tasklet tasklet = (contribution, chunkContext) -> {
			switchPort.switchTo("kmBertEmbeddingLoadingAdapter");
			return RepeatStatus.FINISHED;
		};

		return new StepBuilder("switchModelStepToKmbert", jobRepository)
			.tasklet(tasklet, tx)
			.build();
	}

	@Bean
	public Step switchModelStepToKrsbert(JobRepository jobRepository,
		PlatformTransactionManager tx,
		EmbeddingSwitchPort switchPort) {

		Tasklet tasklet = (contribution, chunkContext) -> {
			switchPort.switchTo("krSBertEmbeddingLoadingAdapter");
			return RepeatStatus.FINISHED;
		};

		return new StepBuilder("switchModelStepToKrsbert", jobRepository)
			.tasklet(tasklet, tx)
			.build();
	}

	@Bean
	public Step gptEmbedSlaveStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<DrugRawDataEntity> reader,
		ItemProcessor<DrugRawDataEntity, DrugVectorDto> processor,
		ItemWriter<DrugVectorDto> writer) {

		return new StepBuilder("gptEmbedSlaveStep", jobRepository)
			.<DrugRawDataEntity, DrugVectorDto>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.build();
	}

	@Bean
	public Step gptEmbedStep(
		JobRepository jobRepository,
		Step gptEmbedSlaveStep) {

		return new StepBuilder("gptEmbedStep", jobRepository)
			.partitioner(gptEmbedSlaveStep.getName(), drugIdRangePartitioner)
			.step(gptEmbedSlaveStep)
			.taskExecutor(taskExecutor)
			.gridSize(10)
			.build();
	}


	@Bean
	public Step kmbertEmbedSlaveStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<DrugRawDataEntity> reader,
		ItemProcessor<DrugRawDataEntity, DrugVectorDto> processor,
		ItemWriter<DrugVectorDto> writer) {

		return new StepBuilder("kmbertEmbedSlaveStep", jobRepository)
			.<DrugRawDataEntity, DrugVectorDto>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.build();
	}

	@Bean
	public Step kmbertEmbedStep(
		JobRepository jobRepository,
		Step kmbertEmbedSlaveStep) {

		return new StepBuilder("kmbertEmbedStep", jobRepository)
			.partitioner(kmbertEmbedSlaveStep.getName(), drugIdRangePartitioner)
			.step(kmbertEmbedSlaveStep)
			.taskExecutor(taskExecutor)
			.gridSize(10)
			.build();
	}

	@Bean
	public Step krsbertEmbedSlaveStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<DrugRawDataEntity> reader,
		ItemProcessor<DrugRawDataEntity, DrugVectorDto> processor,
		ItemWriter<DrugVectorDto> writer) {

		return new StepBuilder("krsbertEmbedSlaveStep", jobRepository)
			.<DrugRawDataEntity, DrugVectorDto>chunk(100, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.build();
	}

	@Bean
	public Step krsbertEmbedStep(
		JobRepository jobRepository,
		Step krsbertEmbedSlaveStep) {
		return new StepBuilder("krsbertEmbedStep", jobRepository)
			.partitioner(krsbertEmbedSlaveStep.getName(), drugIdRangePartitioner)
			.step(krsbertEmbedSlaveStep)
			.taskExecutor(taskExecutor)
			.gridSize(10)
			.build();
	}
	@Bean
	@StepScope
	public JpaPagingItemReader<DrugRawDataEntity> embedItemReader(
		@Value("#{stepExecutionContext['minId']}") Long minId,
		@Value("#{stepExecutionContext['maxId']}") Long maxId,
		EntityManagerFactory emf
	) {
		LogUtil.log(Thread.currentThread().getName() + "Item Read: " + minId + "~" + maxId);
		return new JpaPagingItemReaderBuilder<DrugRawDataEntity>()
			.name("partitionedReader")
			.entityManagerFactory(emf)
			.queryString("SELECT d FROM DrugRawDataEntity d WHERE d.drugId BETWEEN :minId AND :maxId ORDER BY d.drugId")
			.parameterValues(Map.of("minId", minId, "maxId", maxId))
			.pageSize(100)
			.saveState(false)
			.build();
	}



}
