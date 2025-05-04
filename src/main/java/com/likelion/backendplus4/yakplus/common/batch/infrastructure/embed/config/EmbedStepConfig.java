package com.likelion.backendplus4.yakplus.common.batch.infrastructure.embed.config;

import com.likelion.backendplus4.yakplus.common.batch.infrastructure.embed.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.common.batch.infrastructure.embed.reader.DrugIdRangePartitioner;
import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.switcher.application.port.out.EmbeddingSwitchPort;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

/**
 * 임베딩 배치 작업에서 사용되는 Step들을 정의하는 Spring Batch 설정 클래스입니다.
 * <p>
 * 각 임베딩 모델(OpenAI, KM-BERT, KR-SBERT)에 대해 다음과 같은 Step 구조를 가집니다:
 * <ul>
 *     <li>모델 스위칭 Step(Tasklet)</li>
 *     <li>분산 처리를 위한 Master Step + Slave Step (partition + chunk 기반)</li>
 * </ul>
 * <p>
 * 또한, 각 Slave Step은 JPA Paging Reader와 Retry/Skip 정책이 적용된 chunk 기반 처리 구조입니다.
 * Partitioning을 위해 Drug ID 범위 기반 파티셔너가 사용되며,
 * 병렬 처리를 위해 TaskExecutor가 주입됩니다.
 * <p>
 * <p>
 * batchExecutorName: 멀티스레드 처리를 위한 TaskExecutor 이름
 * openAiEmbeddingAdapterName: OpenAI 임베딩 어댑터 이름
 * kmBertEmbeddingAdapterName: KM-BERT 임베딩 어댑터 이름
 * krSBertEmbeddingAdapterName: KR-SBERT 임베딩 어댑터 이름
 * retryLimit: 재시도 횟수
 * retryException: 재시도할 예외 클래스
 * modelSwitchStepName: 모델 스위칭 Step 이름
 * openAiStepName: OpenAI 임베딩 Step 이름
 * kmBertStepName: KM-BERT 임베딩 Step 이름
 * krSBertStepName: KR-SBERT 임베딩 Step 이름
 * rawDataSelectQuery: 원본 데이터 조회 쿼리
 *
 * @since 2025-05-02
 */
@Configuration
public class EmbedStepConfig {
    private final String batchExecutorName = "singleItemExecutor";

    private final String openAiEmbeddingAdapterName = "OpenAiEmbeddingAdapter";
    private final String kmBertEmbeddingAdapterName = "KmBertEmbeddingAdapter";
    private final String krSBertEmbeddingAdapterName = "KrSBertEmbeddingAdapter";

    private final int retryLimit = 3;
    private final Class retryException = Exception.class;

    private final String modelSwitchStepName = "switchModelStep";

    private final String openAiStepName = "openAiEmbedStep";
    private final String kmBertStepName = "kmBertEmbedStep";
    private final String krSBertStepName = "krSBertEmbedStep";
    private final String rawDataSelectQuery = "SELECT d FROM DrugRawDataEntity d WHERE d.drugId BETWEEN :minId AND :maxId ORDER BY d.drugId";


    private final DrugIdRangePartitioner drugIdRangePartitioner;
    private final TaskExecutor taskExecutor;

    public EmbedStepConfig(DrugIdRangePartitioner drugIdRangePartitioner,
                           @Qualifier(batchExecutorName)
                           TaskExecutor taskExecutor) {
        this.drugIdRangePartitioner = drugIdRangePartitioner;
        this.taskExecutor = taskExecutor;
    }

    /**
     * OpenAI 임베딩 모델로 전환하는 Tasklet 기반 Step입니다.
     *
     * @param jobRepository: JobRepository 인스턴스
     * @param tx:            트랜잭션 매니저
     * @param switchPort:    EmbeddingSwitchPort 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step switchModelStepToOpenAi(JobRepository jobRepository,
                                        PlatformTransactionManager tx,
                                        EmbeddingSwitchPort switchPort) {

        Tasklet tasklet = (contribution, chunkContext) -> {
            switchPort.switchTo(openAiEmbeddingAdapterName);
            return RepeatStatus.FINISHED;
        };

        return new StepBuilder(modelSwitchStepName + "ToOpenAi", jobRepository)
                .tasklet(tasklet, tx)
                .build();
    }

    /**
     * KM-BERT 임베딩 모델로 전환하는 Tasklet 기반 Step입니다.
     *
     * @param jobRepository: JobRepository 인스턴스
     * @param tx:            트랜잭션 매니저
     * @param switchPort:    EmbeddingSwitchPort 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step switchModelStepToKmBert(JobRepository jobRepository,
                                        PlatformTransactionManager tx,
                                        EmbeddingSwitchPort switchPort) {

        Tasklet tasklet = (contribution, chunkContext) -> {
            switchPort.switchTo(kmBertEmbeddingAdapterName);
            return RepeatStatus.FINISHED;
        };

        return new StepBuilder(modelSwitchStepName + "ToKmBert", jobRepository)
                .tasklet(tasklet, tx)
                .build();
    }

    /**
     * KR-SBERT 임베딩 모델로 전환하는 Tasklet 기반 Step입니다.
     *
     * @param jobRepository: JobRepository 인스턴스
     * @param tx:            트랜잭션 매니저
     * @param switchPort:    EmbeddingSwitchPort 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step switchModelStepToKrSBert(JobRepository jobRepository,
                                         PlatformTransactionManager tx,
                                         EmbeddingSwitchPort switchPort) {

        Tasklet tasklet = (contribution, chunkContext) -> {
            switchPort.switchTo(krSBertEmbeddingAdapterName);
            return RepeatStatus.FINISHED;
        };

        return new StepBuilder(modelSwitchStepName + "ToKrSbert", jobRepository)
                .tasklet(tasklet, tx)
                .build();
    }

    /**
     * OpenAI 임베딩을 수행하는 Slave Step입니다.
     * chunk 기반으로 엔티티를 읽고 임베딩 벡터로 변환하여 저장합니다.
     *
     * @param jobRepository:      JobRepository 인스턴스
     * @param transactionManager: 트랜잭션 매니저
     * @param reader:             JpaPagingItemReader 인스턴스
     * @param processor:          ItemProcessor 인스턴스
     * @param writer:             ItemWriter 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step openAiEmbedStepSlave(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<DrugRawDataEntity> reader,
            ItemProcessor<DrugRawDataEntity, DrugVectorDto> processor,
            ItemWriter<DrugVectorDto> writer) {

        return new StepBuilder(openAiStepName + "Slave", jobRepository)
                .<DrugRawDataEntity, DrugVectorDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    /**
     * OpenAI 임베딩을 병렬로 수행하는 Master Step입니다.
     * 각 Slave Step을 파티셔닝하여 병렬로 처리합니다.
     *
     * @param jobRepository:        JobRepository 인스턴스
     * @param openAiEmbedStepSlave: Slave Step 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step openAiEmbedStep(
            JobRepository jobRepository,
            Step openAiEmbedStepSlave) {

        return new StepBuilder(openAiStepName, jobRepository)
                .partitioner(openAiEmbedStepSlave.getName(), drugIdRangePartitioner)
                .step(openAiEmbedStepSlave)
                .taskExecutor(taskExecutor)
                .gridSize(10)
                .build();
    }

    /**
     * KM-BERT 임베딩을 수행하는 Slave Step입니다.
     * chunk 기반으로 엔티티를 읽고 임베딩 벡터로 변환하여 저장합니다.
     *
     * @param jobRepository:      JobRepository 인스턴스
     * @param transactionManager: 트랜잭션 매니저
     * @param reader:             JpaPagingItemReader 인스턴스
     * @param processor:          ItemProcessor 인스턴스
     * @param writer:             ItemWriter 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step kmBertEmbedStepSlave(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<DrugRawDataEntity> reader,
            ItemProcessor<DrugRawDataEntity, DrugVectorDto> processor,
            ItemWriter<DrugVectorDto> writer) {

        return new StepBuilder(kmBertStepName + "Slave", jobRepository)
                .<DrugRawDataEntity, DrugVectorDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    /**
     * KM-BERT 임베딩을 병렬로 수행하는 Master Step입니다.
     * 각 Slave Step을 파티셔닝하여 병렬로 처리합니다.
     *
     * @param jobRepository:        JobRepository 인스턴스
     * @param kmBertEmbedStepSlave: Slave Step 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step kmBertEmbedStep(
            JobRepository jobRepository,
            Step kmBertEmbedStepSlave) {

        return new StepBuilder(kmBertStepName, jobRepository)
                .partitioner(kmBertEmbedStepSlave.getName(), drugIdRangePartitioner)
                .step(kmBertEmbedStepSlave)
                .taskExecutor(taskExecutor)
                .gridSize(10)
                .build();
    }

    /**
     * KR-SBERT 임베딩을 수행하는 Slave Step입니다.
     * chunk 기반으로 엔티티를 읽고 임베딩 벡터로 변환하여 저장합니다.
     *
     * @param jobRepository:      JobRepository 인스턴스
     * @param transactionManager: 트랜잭션 매니저
     * @param reader:             JpaPagingItemReader 인스턴스
     * @param processor:          ItemProcessor 인스턴스
     * @param writer:             ItemWriter 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step krSBertEmbedStepSlave(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<DrugRawDataEntity> reader,
            ItemProcessor<DrugRawDataEntity, DrugVectorDto> processor,
            ItemWriter<DrugVectorDto> writer) {

        return new StepBuilder(krSBertStepName + "Slave", jobRepository)
                .<DrugRawDataEntity, DrugVectorDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(3)
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }

    /**
     * KR-SBERT 임베딩을 병렬로 수행하는 Master Step입니다.
     * 각 Slave Step을 파티셔닝하여 병렬로 처리합니다.
     *
     * @param jobRepository:         JobRepository 인스턴스
     * @param krSBertEmbedStepSlave: Slave Step 인스턴스
     * @return Step 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    public Step krSBertEmbedStep(
            JobRepository jobRepository,
            Step krSBertEmbedStepSlave) {
        return new StepBuilder(krSBertStepName, jobRepository)
                .partitioner(krSBertEmbedStepSlave.getName(), drugIdRangePartitioner)
                .step(krSBertEmbedStepSlave)
                .taskExecutor(taskExecutor)
                .gridSize(10)
                .build();
    }

    /**
     * 파티션 내 DrugRawDataEntity를 읽기 위한 JPA 기반 ItemReader입니다.
     * 각 파티션의 minId와 maxId를 기준으로 데이터를 읽어옵니다.
     *
     * @param minId: 파티션의 최소 Drug ID
     * @param maxId: 파티션의 최대 Drug ID
     * @param emf:   EntityManagerFactory 인스턴스
     * @return JpaPagingItemReader 인스턴스
     * @author 함예정
     * @since 2025-05-02
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<DrugRawDataEntity> embedItemReader(
            @Value("#{stepExecutionContext['minId']}") Long minId,
            @Value("#{stepExecutionContext['maxId']}") Long maxId,
            EntityManagerFactory emf) {
        LogUtil.log(Thread.currentThread().getName() + "Item Read: " + minId + "~" + maxId);
        return new JpaPagingItemReaderBuilder<DrugRawDataEntity>()
                .name("partitionedReader")
                .entityManagerFactory(emf)
                .queryString(rawDataSelectQuery)
                .parameterValues(Map.of("minId", minId, "maxId", maxId))
                .pageSize(100)
                .saveState(false)
                .build();
    }
}
