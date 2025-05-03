package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageJobConfig {
	@Bean
	public Job drugImageScrapJob(JobRepository jobRepository,
		Step imageTotalPageCheckStep,
		Step imageMasterStep) {
		return new JobBuilder("drugImageScrapJob", jobRepository)
			.start(imageTotalPageCheckStep)
			.next(imageMasterStep)
			.build();
	}

}
