package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.processor;

import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.index.application.port.out.EmbeddingPort;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.support.mapper.DrugFieldTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 의약품의 효능 정보를 기반으로 임베딩 벡터를 생성하는 Spring Batch ItemProcessor입니다.
 * <p>
 * 입력으로 받은 DrugRawDataEntity에서 효능 정보를 추출 및 전처리하여,
 * EmbeddingLoadingPort를 통해 임베딩 벡터를 생성한 후 DrugVectorDto로 변환합니다.
 *
 * @field embeddingPort EmbeddingPort 인터페이스를 통해 임베딩 벡터를 생성하는 포트
 * @since 2025-04-22
 */
@Component
@RequiredArgsConstructor
public class EmbedProcessor implements ItemProcessor<DrugRawDataEntity, DrugVectorDto> {
    private final EmbeddingPort embeddingPort;

    /**
     * 효능 정보를 추출하고, 해당 텍스트를 임베딩하여 DrugVectorDto로 반환합니다.
     *
     * @param item 임베딩 대상 의약품 엔티티
     * @return 임베딩 벡터를 포함한 DTO
     * @author 이해창
     * @modified 2025-05-02: 함예정
     * - 스프링 배치 적용
     * @since 2025-04-22
     */
    @Override
    public DrugVectorDto process(DrugRawDataEntity item) {
        Long id = item.getDrugId();
        String embeddingText = getEmbedTextFromItem(item);
        float[] embeddingVector = embeddingPort.getEmbedding(embeddingText);
        return DrugVectorDto
                .builder()
                .drugId(id)
                .vector(embeddingVector)
                .build();
    }

    /**
     * DrugRawDataEntity의 효능 정보를 추출하고 전처리하여 임베딩 입력 텍스트로 변환합니다.
     *
     * @param item 임베딩 대상 의약품 엔티티
     * @return String  전처리된 임베딩 입력 텍스트
     * @author 이해창
     * @modified 2025-05-02: 함예정
     * - 스프링 배치 적용
     * @since 2025-04-22
     */
    private String getEmbedTextFromItem(DrugRawDataEntity item) {
        return DrugFieldTypeMapper.convertSingleStringForEfficacy(
                DrugFieldTypeMapper.parseStringToList(item.getEfficacy())
        );
    }
}
