package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.config;

import java.util.List;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.api.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.util.MdcTaskDecorator;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.processor.ImageScrapProcessor;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.processor.ImageTotalPageCalculator;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.reader.PageRangePartitioner;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.reader.PartitionedPageReader;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.writer.DrugImageWriter;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.mapper.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.ApiDataDrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.ApiDataDrugImgRepo;

/**
 * 의약품 상세정보 수집을 위한 Spring Batch 설정 클래스
 */
@Configuration
public class ImageStepConfig {
	private final PartitionedPageReader imagePageNumberReader;
	private final PageRangePartitioner pageRangePartitioner;
	private final ApiRequestManager apiRequestManager;
	private final ApiResponseMapper apiResponseMapper;
	private final TaskExecutor taskExecutor;

	public ImageStepConfig(PartitionedPageReader imagePageNumberReader,
						  ApiRequestManager apiRequestManager,
						  MdcTaskDecorator mdcTaskDecorator,
		                  ApiResponseMapper apiResponseMapper,
		                  @Qualifier("batchExecutorManyThread")
		                  TaskExecutor taskExecutor,
		PageRangePartitioner pageRangePartitioner) {
		this.imagePageNumberReader = imagePageNumberReader;
		this.apiRequestManager = apiRequestManager;
		this.apiResponseMapper = apiResponseMapper;
		this.taskExecutor = taskExecutor;
		this.pageRangePartitioner = pageRangePartitioner;
	}



	/**
	 * 전체 페이지 수를 계산하는 Tasklet 기반 Step 정의
	 *
	 * @param jobRepository JobRepository 인스턴스
	 * @param txManager 트랜잭션 매니저
	 * @param imageTotalPageCalculator 페이지 계산 로직을 수행하는 Tasklet
	 * @return Step 인스턴스
	 *
	 * @author 함예정
	 * @since 2025-05-01
	 */
	@Bean
	Step imageTotalPageCheckStep(JobRepository jobRepository, PlatformTransactionManager txManager,
		ImageTotalPageCalculator imageTotalPageCalculator) {
		return new StepBuilder("imageTotalPageCheck", jobRepository)
			.tasklet(imageTotalPageCalculator, txManager)
			.build();
	}

	@Bean
	public Step imageMasterStep(JobRepository jobRepository,
		PlatformTransactionManager txManager,
		PageRangePartitioner partitioner,
		Step imageScrapStep) {
		return new StepBuilder("imageMasterStep", jobRepository)
			.partitioner(imageScrapStep.getName(), partitioner)
			.step(imageScrapStep)
			.gridSize(15)
			.taskExecutor(taskExecutor)
			.build();
	}

	/**
	 * 상세 페이지별로 데이터를 수집하고 처리 및 저장하는 Chunk 기반 Step 정의
	 *
	 * @param jobRepository JobRepository 인스턴스
	 * @param txManager 트랜잭션 매니저
	 * @param processor 데이터를 처리하는 Processor
	 * @param writer 데이터를 저장하는 Writer
	 * @return Step 인스턴스
	 *
	 * @author 함예정
	 * @since 2025-05-01
	 */
	@Bean
	public Step imageScrapStep(JobRepository jobRepository,
		PlatformTransactionManager txManager,
		ImageScrapProcessor processor,
		DrugImageWriter writer) {
		return new StepBuilder("imageScrapStep", jobRepository)
			.<Integer, List<ApiDataDrugImgEntity>>chunk(1, txManager)
			.reader(imagePageNumberReader)
			.processor(processor)
			.writer(writer)
			.faultTolerant()
			.retry(Exception.class)
			.retryLimit(3)
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.taskExecutor(taskExecutor)
			.build();
	}

	/**
	 * 의약품 상세정보 처리용 Processor Bean 정의
	 *
	 * @return CombineProcessor 인스턴스
	 *
	 * @author 함예정
	 * @since 2025-05-01
	 */
	@Bean
	public ImageScrapProcessor imageScrapProcessor() {
		return new ImageScrapProcessor(apiRequestManager, apiResponseMapper);
	}

	/**
	 * 처리된 의약품 상세정보를 DB에 저장하는 Writer Bean 정의
	 *
	 * @param repository 의약품 상세정보 저장용 JPA Repository
	 * @return DrugDetailWriter 인스턴스
	 *
	 * @author 함예정
	 * @since 2025-05-01
	 */
	@Bean
	public DrugImageWriter imageScrapWriter(ApiDataDrugImgRepo repository) {
		return new DrugImageWriter(repository);
	}
}
