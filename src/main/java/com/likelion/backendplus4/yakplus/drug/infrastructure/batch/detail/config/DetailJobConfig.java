package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DetailJobConfig {
	@Bean
	public Job drugDetailScrapJob(JobRepository jobRepository,
		Step totalPageCheckStep,
		Step drugDetailStep) {
		return new JobBuilder("drugDetailScrapJob", jobRepository)
			.start(totalPageCheckStep)
			.next(drugDetailStep)
			.build();
	}
}
