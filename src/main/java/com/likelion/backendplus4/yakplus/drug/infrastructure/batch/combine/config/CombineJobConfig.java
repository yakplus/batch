package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CombineJobConfig {
	@Bean
	public Job drugTableCombineJob(JobRepository jobRepository,
		Step tableCombineStep) {
		return new JobBuilder("drugTableCombineJob", jobRepository)
			.start(tableCombineStep)
			.build();
	}
}
