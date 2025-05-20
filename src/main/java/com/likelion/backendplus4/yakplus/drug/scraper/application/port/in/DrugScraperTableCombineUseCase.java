package com.likelion.backendplus4.yakplus.drug.scraper.application.port.in;

public interface DrugScraperTableCombineUseCase {

	/**
	 * API 요청으로 받아온 RAW 데이터 테이블 2개를 병합해
	 * 1개의 테이블로 만드는  기능을 수행합니다.
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 */
	String mergeTable();

	/**
	 * 현재 작업 상태를 조회합니다
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String getStatus();

	/**
	 * 현재 진행 중인 작업을 중지합니다.
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String stop();
}
