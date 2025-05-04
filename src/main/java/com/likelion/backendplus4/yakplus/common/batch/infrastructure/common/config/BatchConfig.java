package com.likelion.backendplus4.yakplus.common.batch.infrastructure.common.config;

import com.likelion.backendplus4.yakplus.common.batch.infrastructure.common.util.MdcTaskDecorator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 의약품 상세정보 수집을 위한 Spring Batch 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final MdcTaskDecorator mdcTaskDecorator;
    private final String normalExecutorName = "normalExecutor";
    private final String singleItemExecutorName = "singleItemExecutor";

    /**
     * 일반적인 작업처리를 위한 ThreadPool 기반 TaskExecutor 설정
     *
     * @return TaskExecutor 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean(normalExecutorName)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10);
        executor.setTaskDecorator(mdcTaskDecorator);
        executor.setThreadNamePrefix("batch-task-");
        executor.initialize();
        return executor;
    }

    /**
     * 단일 아이템 단위로 처리하는 작업을 위한 
     * ThreadPool 기반 TaskExecutor 설정
     *
     * @return TaskExecutor 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean(singleItemExecutorName)
    public TaskExecutor taskExecutorMoreThreads() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setTaskDecorator(mdcTaskDecorator);
        executor.setThreadNamePrefix("batch-task(m)-");
        executor.initialize();
        return executor;
    }
}
