package com.likelion.backendplus4.yakplus.common.batch.infrastructure.combine.config;

import com.likelion.backendplus4.yakplus.common.batch.infrastructure.combine.dto.TableCombineDto;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 의약품 상세 정보와 이미지 정보를 병합하여 DrugRawDataEntity로 저장하는 Step 설정 클래스입니다.
 * <p>
 * DrugDetailEntity와 ApiDataDrugImgEntity를 조인하여 TableCombineDto로 읽고,
 * 이를 DrugRawDataEntity로 변환한 뒤 저장합니다.
 *
 * @field combineStepName        테이블 병합 Step의 이름
 * @field drugDetailReaderName   ItemReader의 이름
 * @field tableCombineEntityFqn  병합 테이블 DTO의 경로 Full Path
 * @field drugDetailEntity       의약품 상세 정보 Entity 클래스명
 * @field drugImageEntity        의약품 이미지 Entity 클래스명
 * @field detailAlias            의약품 상세 정보 Entity의 별칭
 * @field imageAlias             의약품 이미지 Entity의 별칭
 * @field RETRY_LIMIT            재시도 횟수 제한 설정 값
 * @field SKIP_LIMIT             스킵 횟수 제한 설정 값
 * @field PAGE_SIZE              페이지 크기
 * @since 2025-05-02
 */
@Configuration
public class CombineStepConfig {
    private static final String COMBINE_STEP_NAME = "drugCombineStep";
    private static final String DRUG_DETAIL_READER_NAME = "drugDetailReader";

    private static final String TABLE_COMBINE_ENTITY_FQN =
            "com.likelion.backendplus4.yakplus.common.batch.infrastructure.combine.dto.TableCombineDto";
    private static final String DRUG_DETAIL_ENTITY = "DrugDetailEntity";
    private static final String DRUG_IMAGE_ENTITY = "ApiDataDrugImgEntity";
    private static final String DETAIL_ALIAS = "d";
    private static final String IMAGE_ALIAS = "i";
    private static final int RETRY_LIMIT = 3;
    private static final int SKIP_LIMIT = 5_000;
    private static final int PAGE_SIZE = 1000;

    /**
     * 병합 작업을 수행하는 Step을 정의합니다.
     * <p>
     * TableCombineDto → DrugRawDataEntity로 변환하여 저장하며, 예외 허용 정책도 포함됩니다.
     *
     * @param jobRepository      Job 저장소
     * @param transactionManager 트랜잭션 관리자
     * @param reader             병합 대상 데이터를 읽는 Reader
     * @param processor          병합 및 매핑 처리기
     * @param writer             결과 데이터를 저장하는 Writer
     * @return 구성된 Step
     */
    @Bean
    public Step tableCombineStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<TableCombineDto> reader,
            ItemProcessor<TableCombineDto, DrugRawDataEntity> processor,
            ItemWriter<DrugRawDataEntity> writer) {
        return new StepBuilder(COMBINE_STEP_NAME, jobRepository)
                .<TableCombineDto, DrugRawDataEntity>chunk(PAGE_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(RETRY_LIMIT)
                .skip(Exception.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    /**
     * 의약품 상세 정보와 이미지 정보를 병합하여 TableCombineDto로 읽어오는 Reader입니다.
     * <p>
     * DrugDetailEntity와 ApiDataDrugImgEntity를 LEFT JOIN하여 필요한 필드를 조합합니다.
     *
     * @param entityManagerFactory JPA EntityManagerFactory
     * @return JpaPagingItemReader JPA 페이징 ItemReader
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<TableCombineDto> drugDetailReader(EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<TableCombineDto> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(getJoinTableSql());
        reader.setPageSize(PAGE_SIZE);
        reader.setSaveState(true);
        reader.setName(DRUG_DETAIL_READER_NAME);
        return reader;
    }

    /**
     * DrugDetailEntity와 ApiDataDrugImgEntity를 조인하여 TableCombineDto를 생성하는 JPQL 쿼리를 반환합니다.
     *
     * @return 병합을 위한 JPQL 쿼리 문자열
     */
    private String getJoinTableSql() {
        return String.format("""
        SELECT new %s(
            %s.drugId,
            %s.drugName,
            %s.company,
            %s.permitDate,
            %s.isGeneral,
            %s.materialInfo,
            %s.storeMethod,
            %s.validTerm,
            %s.efficacy,
            %s.usage,
            %s.precaution,
            %s.cancelDate,
            %s.cancelName,
            %s.isHerbal,
            %s.productImage,
            %s.pillImage
        )
        FROM %s %s
        LEFT JOIN %s %s ON %s.drugId = %s.drugId
        """,
                TABLE_COMBINE_ENTITY_FQN,
                DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS,
                DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS,
                DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS, DETAIL_ALIAS,
                IMAGE_ALIAS, IMAGE_ALIAS,
                DRUG_DETAIL_ENTITY, DETAIL_ALIAS,
                DRUG_IMAGE_ENTITY, IMAGE_ALIAS,
                DETAIL_ALIAS, IMAGE_ALIAS
        );
    }
}
