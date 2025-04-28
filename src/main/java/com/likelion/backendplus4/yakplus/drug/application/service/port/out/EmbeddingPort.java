package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;

/**
 * 텍스트 임베딩을 위한 포트 인터페이스입니다.
 * 다양한 임베딩 모델을 사용하여 텍스트를 벡터(float 배열)로 변환합니다.
 *
 * @since 2025-04-25
 */
public interface EmbeddingPort {

	/**
	 * 주어진 텍스트를 지정한 임베딩 모델을 사용하여 임베딩 벡터(float 배열)로 변환합니다.
	 *
	 * @param text 임베딩할 텍스트
	 * @param modelType 사용할 임베딩 모델 타입 (enum)
	 * @return 임베딩된 벡터 (float 배열)
	 *
	 * @author 이해창
	 * @since 2025-04-25
	 */
	float[] getEmbedding(String text, EmbeddingModelType modelType);
}
