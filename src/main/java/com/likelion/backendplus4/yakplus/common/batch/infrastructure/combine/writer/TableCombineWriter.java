package com.likelion.backendplus4.yakplus.common.batch.infrastructure.combine.writer;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * DrugRawDataEntity 리스트를 JPA를 통해 저장하는 Spring Batch ItemWriter 구현체입니다.
 * <p>
 * 테이블 병합 후 생성된 엔티티를 DB에 일괄 저장하며, 처리 건수를 로그로 출력합니다.
 *
 * @fields drugRawDataRepository JPA를 통해 DrugRawDataEntity를 저장하는 레포지토리
 * @fields count                 저장된 엔티티의 개수를 세기 위한 AtomicInteger
 * @since 2025-05-02
 */
@Component
@StepScope
@RequiredArgsConstructor
public class TableCombineWriter implements ItemWriter<DrugRawDataEntity> {
    private final GovDrugJpaRepository drugRawDataRepository;
    private final AtomicInteger count = new AtomicInteger();

    /**
     * 병합된 DrugRawDataEntity 리스트를 JPA를 통해 저장합니다.
     *
     * @param entity Chunk 단위로 전달된 엔티티 목록
     * @since 2025-05-02
     * @author 함예정
     */
    @Override
    public void write(Chunk<? extends DrugRawDataEntity> entity) {
        List<DrugRawDataEntity> items = new ArrayList<>(entity.getItems());
        drugRawDataRepository.saveAll(items);
        log("테이블 병합 작업 - 쓰기 완료: " + count.addAndGet(items.size()));

    }
}
