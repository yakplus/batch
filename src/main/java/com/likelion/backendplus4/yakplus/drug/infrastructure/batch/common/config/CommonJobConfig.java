package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.common.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CommonJobConfig {
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
		Step imageMasterStep,
		Step switchModelStepToGpt,
		Step gptEmbedStep,
		Step switchModelStepToKmbert,
		Step kmbertEmbedStep,
		Step switchModelStepToKrsbert,
		Step krsbertEmbedStep) {
		return new JobBuilder("drugScrapJob", jobRepository)
			.start(totalPageCheckStep)
			.next(drugDetailStep)
			.next(imageTotalPageCheckStep)
			.next(imageMasterStep)
			.next(switchModelStepToGpt)
			.next(gptEmbedStep)
			.next(switchModelStepToKmbert)
			.next(kmbertEmbedStep)
			.next(switchModelStepToKrsbert)
			.next(krsbertEmbedStep)
			.build();
	}
}
