package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.embed.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class EmbedJobConfig {
	@Bean
	public Job embedJob(JobRepository jobRepository,
		Step switchModelStepToGpt,
		Step gptEmbedStep,
		Step switchModelStepToKmbert,
		Step kmbertEmbedStep,
		Step switchModelStepToKrsbert,
		Step krsbertEmbedStep){
		return new JobBuilder("embedJob", jobRepository)
			.start(switchModelStepToGpt)
			.next(gptEmbedStep)
			.next(switchModelStepToKmbert)
			.next(kmbertEmbedStep)
			.next(switchModelStepToKrsbert)
			.next(krsbertEmbedStep)
			.build();
	}
}
