package com.likelion.backendplus4.yakplus.drug.embed.application.port.in;

/**
 * 의약품 효능 정보를 임베딩 처리를 위한 유스케이스 인터페이스입니다.
 *
 * @since 2025-04-25
 * @modify 2025-05-02 함예정
 *   - 스프링 배치로 전환, 배치 포트로 외부서비스 요청하도록 변경
 */
public interface DrugEmbedProcessorUseCase {

    /**
     * 의약품 데이터를 기반으로 임베딩 프로세스를 시작합니다.
     *
     * @since 2025-04-25
     * @modify 2025-05-02 함예정
     */
    String startEmbedding();

    /**
     * 임베딩 모델을 전환합니다.
     *
     * @param modelType 전환할 임베딩 모델의 타입
     *                  (예: gptEmbeddingLoadingAdapter, kmBertEmbeddingLoadingAdapter, krSBertEmbeddingLoadingAdapter)
     *
     * @author 정안식
     * @since 2025-04-25
     * @modify 2025-05-02 함예정
     */
    void switchEmbeddingModel(String modelType);

    /**
     * 현재 사용 중인 임베딩 모델을 조회하는 메서드입니다.
     *
     * @return 현재 사용 중인 임베딩 모델
     *
     * @author 정안식
     * @since 2025-04-25
     * @modify 2025-05-02 함예정
     */
    String getCurrentEmbeddingModel();

    /**
     * 현재 임베딩 작업의 상태를 조회합니다.
     *
     * @return 임베딩 작업 상태 메시지
     *
     * @author 함예정
     * @since 2025-05-02
     */
    String statusEmbedding();

    /**
     * 실행 중인 임베딩 작업을 중지합니다.
     *
     * @return 임베딩 작업 중지 결과 메시지
     *
     * @author 함예정
     * @since 2025-05-02
     */
    String stopEmbedding();
}
