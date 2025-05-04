package com.likelion.backendplus4.yakplus.common.batch.infrastructure.detail.writer;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugDetailJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 의약품 상세정보 Entity 리스트를 JPA Repository를 통해 일괄 저장하는 Writer입니다.
 * <p>
 * Chunk 내부의 각 List<DrugDetailEntity> 항목을 반복 처리하며,
 * 각 리스트는 한 API 페이지에서 파싱된 다수의 의약품 데이터를 나타냅니다.
 * <p>
 * GovDrugDetailJpaRepository: 의약품 상세정보를 저장하는 JPA Repository
 *
 * @field repository 의약품 상세정보를 저장하는 JPA Repository
 * @since 2025-05-02
 */

@Component
@RequiredArgsConstructor
public class DrugDetailWriter implements ItemWriter<List<DrugDetailEntity>> {

    private final GovDrugDetailJpaRepository repository;

    public DrugDetailWriter(GovDrugDetailJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Chunk 단위로 전달된 Entity 리스트를 순회하며 JPA를 통해 일괄 저장합니다.
     *
     * @param chunk 페이지별로 수집된 의약품 상세정보 엔티티 리스트
     * @author 함예정
     * @since 2025-05-02
     */
    @Override
    public void write(Chunk<? extends List<DrugDetailEntity>> chunk) {

        for (List<DrugDetailEntity> items : chunk.getItems()) {
            repository.saveAll(items);
        }
    }
}
