package com.likelion.backendplus4.yakplus.drug.application.service.port.in.scraper;

public interface DrugScraperUsecase {

	/**
	 * 의약품 데이터 수집 및 임베딩 프로세스를 순차적으로 실행합니다.
	 * 1. 상세 정보 수집 후 RDB 테이블 저장
	 * 2. 이미지 정보 수집 후 RDB 테이블 저장
	 * 3. 상세 정보와 이미지 병합 후 통합 테이블 저장
	 * 4. 임베딩 벡터 생성 및 각각 벡터 테이블 저장
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 */
	String scraperStart();

	/**
	 * 현재 스크래핑 및 임베딩 작업의 상태를 조회합니다.
	 *
	 * @return 현재 작업 상태 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String getStatus();

	/**
	 * 실행 중인 스크래핑 및 임베딩 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String stop();

	/**
	 * 중지된 모든 배치 작업을 조회하고, 다시 실행합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String restart();
}
