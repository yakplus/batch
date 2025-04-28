package com.likelion.backendplus4.yakplus.drug.application.service.port.out;

/**
 * 의약품 임베딩 벡터를 저장 및 조회하는 리포지토리 포트 인터페이스입니다.
 * 다양한 임베딩 모델(GPT, KmBERT, KorSBERT)의 벡터를 저장하고 조회하는 기능을 제공합니다.
 *
 * @since 2025-04-21
 */
public interface DrugEmbedRepositoryPort {

	/**
	 * GPT 기반 임베딩 벡터를 저장합니다.
	 *
	 * @param drugId 임베딩할 의약품 ID
	 * @param vector GPT 임베딩 벡터
	 *
	 * @since 2025-04-21
	 */
	void saveGptEmbed(Long drugId, float[] vector);

	/**
	 * KmBERT 기반 임베딩 벡터를 저장합니다.
	 *
	 * @param drugId 임베딩할 의약품 ID
	 * @param gptVector KmBERT 임베딩 벡터
	 *
	 * @since 2025-04-21
	 */
	void saveKmBertEmbed(Long drugId, float[] gptVector);

	/**
	 * KrSBERT 기반 임베딩 벡터를 저장합니다.
	 *
	 * @param drugId 임베딩할 의약품 ID
	 * @param krSbertVector KorSBERT 임베딩 벡터
	 *
	 * @since 2025-04-21
	 */
	void saveKrSbertEmbed(Long drugId, float[] krSbertVector);
}
