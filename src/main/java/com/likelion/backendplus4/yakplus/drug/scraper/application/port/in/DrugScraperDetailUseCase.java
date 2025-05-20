package com.likelion.backendplus4.yakplus.drug.scraper.application.port.in;
/**
 * 의약품 상세 정보를 수집하는 유스케이스 인터페이스입니다.
 *
 * @since 2025-04-21
 */
public interface DrugScraperDetailUseCase {
	/**
	 * 모든 의약품의 상세 정보를 일괄적으로 요청하여 수집합니다.
	 *
	 * @since 2025-04-21
	 */
	String requestAllData();

	/**
	 * 실행 중인 상세 정보 조회 배치를 중단합니다.
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 */
	String stop();

	/**
	 * 실행 중인 상세 정보 조회 상태를 가져옵니다.
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 */
	String getStatus();
}
