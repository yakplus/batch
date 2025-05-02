package com.likelion.backendplus4.yakplus.drug.application.service.port.in;

import jakarta.transaction.Transactional;

public interface DrugScraperTableCombineUsecase {

	/**
	 * API 요청으로 받아온 RAW 데이터 테이블 2개를 병합해
	 * 1개의 테이블로 만드는  기능을 수행합니다.
	 *
	 * @since 2025-04-21
	 */
	String mergeTable();
}
