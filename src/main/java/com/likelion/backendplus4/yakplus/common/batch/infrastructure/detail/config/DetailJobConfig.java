package com.likelion.backendplus4.yakplus.common.batch.infrastructure.detail.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 의약품 상세정보 수집 배치 작업을 구성하는 설정 클래스입니다.
 * <p>
 * 작업 순서는 아래와 같습니다.
 * 1. 전체 페이지 수를 계산하는 Step: 의약품 상세정보의 전체 페이지 수를 계산합니다.
 * 2. 의약품 상세정보를 수집하는 Step: 계산된 페이지 수를 기반으로 상세정보를 수집합니다.
 *
 * @since 2025-05-02
 */
@Configuration
@RequiredArgsConstructor
public class DetailJobConfig {

    /**
     * 의약품 상세정보 수집을 위한 Job을 정의합니다.
     *
     * @param jobRepository         Job 실행 정보 저장소
     * @param totalPageCheckStep    전체 페이지 수 확인 스텝
     * @param drugDetailStep        의약품 상세정보 수집 스텝
     * @return Job                  구성된 Job 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
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
