package com.likelion.backendplus4.yakplus.common.batch.infrastructure.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 공통 배치 Job 설정을 위한 클래스입니다.
 * <p>
 * 이 클래스는 의약품 정보를 수집하는 배치 Job을 정의합니다.
 * <p>
 * @field jobName Job 이름
 * @since 2025-05-02
 */
@Configuration
@RequiredArgsConstructor
public class CommonJobConfig {

    private final String jobName = "drugScrapJob";

    /**
     * 의약품 정보를 수집하는 Batch Job 정의
     *
     * @param jobRepository      JobRepository 인스턴스
     * @param totalPageCheckStep 전체 페이지 수 확인 Step
     * @param drugDetailStep     상세 정보 수집 Step
     * @return 구성된 Job 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Job drugScrapJob(JobRepository jobRepository,
                            Step totalPageCheckStep,
                            Step drugDetailStep,
                            Step imageTotalPageCheckStep,
                            Step imageMasterStep,
                            Step switchModelStepToOpenAi,
                            Step openAiEmbedStep,
                            Step switchModelStepToKmBert,
                            Step kmBertEmbedStep,
                            Step switchModelStepToKrSBert,
                            Step krSBertEmbedStep) {
        return new JobBuilder(jobName, jobRepository)
                .start(totalPageCheckStep)
                .next(drugDetailStep)
                .next(imageTotalPageCheckStep)
                .next(imageMasterStep)
                .next(switchModelStepToOpenAi)
                .next(openAiEmbedStep)
                .next(switchModelStepToKmBert)
                .next(kmBertEmbedStep)
                .next(switchModelStepToKrSBert)
                .next(krSBertEmbedStep)
                .build();
    }
}
