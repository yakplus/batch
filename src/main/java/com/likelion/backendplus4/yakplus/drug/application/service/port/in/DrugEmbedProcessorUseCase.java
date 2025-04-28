package com.likelion.backendplus4.yakplus.drug.application.service.port.in;

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
}
