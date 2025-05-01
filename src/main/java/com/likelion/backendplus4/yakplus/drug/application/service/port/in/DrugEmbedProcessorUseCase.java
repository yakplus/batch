package com.likelion.backendplus4.yakplus.drug.application.service.port.in;

import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;

/**
 * 의약품 효능 정보를 임베딩 처리를 위한 유스케이스 인터페이스입니다.
 *
 * @since 2025-04-25
 */
public interface DrugEmbedProcessorUseCase {

    /**
     * 의약품 데이터를 기반으로 임베딩 프로세스를 시작합니다.
     *
     * @since 2025-04-25
     */
    void startEmbedding();

    /**
     * 임베딩 모델을 스위칭하는 메서드입니다.
     *
     * @param modelType 전환할 임베딩 모델 타입 (GPT, KmBERT, KrSBERT)
     */
    void switchEmbeddingModel(String modelType);

    /**
     * 현재 사용 중인 임베딩 모델을 조회하는 메서드입니다.
     *
     * @return 현재 사용 중인 임베딩 모델
     */
    EmbeddingModelType getCurrentEmbeddingModel();
}
