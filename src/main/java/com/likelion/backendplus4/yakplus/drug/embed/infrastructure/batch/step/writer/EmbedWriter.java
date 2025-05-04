package com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.writer;

import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.batch.step.dto.DrugVectorDto;
import com.likelion.backendplus4.yakplus.drug.index.application.port.out.EmbeddingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

/**
 * 임베딩 벡터를 외부 저장소 또는 인프라에 저장하는 Spring Batch ItemWriter 구현 클래스입니다.
 * <p>
 * EmbeddingLoadingPort를 통해 전달된 DrugVectorDto 리스트를 저장하고,
 * 처리된 항목 수를 로깅합니다. 주로 벡터 저장 API 호출 또는 벡터 DB 연동 작업에 사용됩니다.
 *
 * @fields embeddingPort 임베딩 저장을 위한 포트
 * @fields count                처리된 항목 수 카운트를 위한 AtomicInteger
 * @since 2025-05-02
 */
@Component
@StepScope
@RequiredArgsConstructor
public class EmbedWriter implements ItemWriter<DrugVectorDto> {
    private final EmbeddingPort embeddingPort;
    private final AtomicInteger count = new AtomicInteger();

    /**
     * 전달된 임베딩 결과 리스트를 저장소에 저장합니다.
     *
     * @param dto 임베딩된 벡터를 포함한 DTO 목록 (Chunk 단위)
     * @since 2025-05-02
     * @author 함예정
     */
    @Override
    public void write(Chunk<? extends DrugVectorDto> dto) {
        List<DrugVectorDto> items = new ArrayList<>(dto.getItems());
        embeddingPort.saveEmbedding(items);
        log("임베딩 작업 - 쓰기 완료: " + count.addAndGet(items.size()));
    }
}
