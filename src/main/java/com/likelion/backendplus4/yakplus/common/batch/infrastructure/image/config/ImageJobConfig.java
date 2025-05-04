package com.likelion.backendplus4.yakplus.common.batch.infrastructure.image.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 의약품 이미지 크롤링 작업을 정의하는 Spring Batch Job 설정 클래스입니다.
 * <p>
 * 총 페이지 수 확인 후, 병렬로 이미지를 수집하는 Step을 실행합니다.
 *
 * @since 2025-05-02
 */
@Configuration
public class ImageJobConfig {


    /**
     * 의약품 이미지 수집을 위한 Job을 정의합니다.
     * <p>
     * Step 순서:
     * <ol>
     *     <li>imageTotalPageCheckStep: 전체 페이지 수를 계산</li>
     *     <li>imageMasterStep: 병렬로 이미지 크롤링 수행</li>
     * </ol>
     *
     * @param jobRepository           Spring Batch JobRepository
     * @param imageTotalPageCheckStep 페이지 수 확인 Step
     * @param imageMasterStep         이미지 수집 Master Step (partition 기반)
     * @return 구성된 Job 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
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
