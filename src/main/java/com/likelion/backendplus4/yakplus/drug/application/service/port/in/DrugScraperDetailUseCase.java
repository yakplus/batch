package com.likelion.backendplus4.yakplus.drug.application.service.port.in;
/**
 * 의약품 상세 정보를 수집하는 유스케이스 인터페이스입니다.
 * 1페이지 요청 및 전체 데이터 요청을 정의합니다.
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
}
