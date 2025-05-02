package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.api.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.util.MdcTaskDecorator;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.reader.DetailPageNumberReader;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.mapper.ApiResponseMapper;

import lombok.RequiredArgsConstructor;

/**
 * 의약품 상세정보 수집을 위한 Spring Batch 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
	private final MdcTaskDecorator mdcTaskDecorator;

	/**
	 * 병렬 처리를 위한 ThreadPool 기반 TaskExecutor 설정
	 *
	 * @return TaskExecutor 인스턴스
	 *
	 * @author 함예정
	 * @since 2025-05-01
	 */
	@Bean("batchExecutor")
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(10);
		executor.setTaskDecorator(mdcTaskDecorator);
		executor.setThreadNamePrefix("batch-task-");
		executor.initialize();
		return executor;
	}

	@Bean("batchExecutorManyThread")
	public TaskExecutor taskExecutorMoreThreads() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(20);
		executor.setTaskDecorator(mdcTaskDecorator);
		executor.setThreadNamePrefix("batch-task(m)-");
		executor.initialize();
		return executor;
	}

	/**
	 * 의약품 정보를 수집하는 Batch Job 정의
	 *
	 * @param jobRepository JobRepository 인스턴스
	 * @param totalPageCheckStep 전체 페이지 수 확인 Step
	 * @param drugDetailStep 상세 정보 수집 Step
	 * @return 구성된 Job 인스턴스
	 *
	 * @author 함예정
	 * @since 2025-05-01
	 */
	@Bean
	public Job drugScrapJob(JobRepository jobRepository,
		Step totalPageCheckStep,
		Step drugDetailStep,
		Step imageTotalPageCheckStep,
		Step imageMasterStep) {
		return new JobBuilder("drugScrapJob", jobRepository)
			.start(totalPageCheckStep)
			.next(drugDetailStep)
			.next(imageTotalPageCheckStep)
			.next(imageMasterStep)
			.build();
	}

	@Bean
	public Job drugDetailScrapJob(JobRepository jobRepository,
		Step totalPageCheckStep,
		Step drugDetailStep) {
		return new JobBuilder("drugDetailScrapJob", jobRepository)
			.start(totalPageCheckStep)
			.next(drugDetailStep)
			.build();
	}
	@Bean
	public Job drugImageScrapJob(JobRepository jobRepository,
		Step imageTotalPageCheckStep,
		Step imageMasterStep) {
		return new JobBuilder("drugImageScrapJob", jobRepository)
			.start(imageTotalPageCheckStep)
			.next(imageMasterStep)
			.build();
	}

	@Bean
	public Job drugTableCombineJob(JobRepository jobRepository,
		Step tableCombineStep) {
		return new JobBuilder("drugTableCombineJob", jobRepository)
			.start(tableCombineStep)
			.build();
	}
}
