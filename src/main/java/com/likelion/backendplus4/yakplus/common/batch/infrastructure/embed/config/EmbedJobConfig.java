package com.likelion.backendplus4.yakplus.common.batch.infrastructure.embed.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 임베딩 처리 작업을 구성하는 Spring Batch 설정 클래스입니다.
 * <p>
 * 모델 스위칭과 임베딩 작업을 순차적으로 구성하여 실행합니다.
 * OpenAI → KM-BERT → KR-SBERT 순서로 각각 임베딩을 수행합니다.
 * 
 * @since 2025-05-02
 */
@Configuration
public class EmbedJobConfig {

    /**
     * 여러 임베딩 모델을 순차적으로 실행하는 Job을 정의합니다.
     * 각 단계는 다음을 포함합니다:
     * <ul>
     *     <li>모델 스위칭 (OpenAI, KM-BERT, KR-SBERT)</li>
     *     <li>각 모델에 대한 임베딩 실행</li>
     * </ul>
     *
     * @param jobRepository Spring Batch JobRepository
     * @param switchModelStepToOpenAi OpenAI 모델로 스위칭하는 Step
     * @param openAiEmbedStep OpenAI 모델을 이용한 임베딩 Step
     * @param switchModelStepToKmBert KM-BERT 모델로 스위칭하는 Step
     * @param kmBertEmbedStep KM-BERT 모델을 이용한 임베딩 Step
     * @param switchModelStepToKrSBert KR-SBERT 모델로 스위칭하는 Step
     * @param krSBertEmbedStep KR-SBERT 모델을 이용한 임베딩 Step
     * @return 구성된 Job 인스턴스
     * 
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Job embedJob(JobRepository jobRepository,
                        Step switchModelStepToOpenAi,
                        Step openAiEmbedStep,
                        Step switchModelStepToKmBert,
                        Step kmBertEmbedStep,
                        Step switchModelStepToKrSBert,
                        Step krSBertEmbedStep) {
        return new JobBuilder("embedJob", jobRepository)
                .start(switchModelStepToOpenAi)
                .next(openAiEmbedStep)
                .next(switchModelStepToKmBert)
                .next(kmBertEmbedStep)
                .next(switchModelStepToKrSBert)
                .next(krSBertEmbedStep)
                .build();
    }
}
