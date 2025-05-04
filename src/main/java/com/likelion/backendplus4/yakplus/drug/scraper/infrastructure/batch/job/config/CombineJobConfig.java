package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 의약품 테이블 통합 작업을 정의하는 Spring Batch Job 설정 클래스입니다.
 * <p>
 * 개별 테이블로부터 수집된 데이터를 통합하여 단일 테이블로 병합하는 작업을 수행합니다.
 *
 * @since 2025-05-02
 */
@Configuration
public class CombineJobConfig {

    /**
     * 테이블 통합 작업을 수행하는 Job을 정의합니다.
     * 단일 Step(tableCombineStep)으로 구성되며, 의약품 관련 데이터 병합 로직을 포함합니다.
     *
     * @param jobRepository    Spring Batch JobRepository
     * @param tableCombineStep 실제 테이블 병합을 수행하는 Step
     * @return Job             구성된 Job 인스턴스
     */
    @Bean
    public Job drugTableCombineJob(JobRepository jobRepository,
                                   Step tableCombineStep) {
        return new JobBuilder("drugTableCombineJob", jobRepository)
                .start(tableCombineStep)
                .build();
    }
}
