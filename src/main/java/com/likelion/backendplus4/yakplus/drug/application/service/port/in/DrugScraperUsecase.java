package com.likelion.backendplus4.yakplus.drug.application.service.port.in;

public interface DrugScraperUsecase {

	/**
	 * 의약품 데이터 수집 및 임베딩 프로세스를 순차적으로 실행합니다.
	 * 1. 상세 정보 수집 후 RDB 테이블 저장
	 * 2. 이미지 정보 수집 후 RDB 테이블 저장
	 * 3. 상세 정보와 이미지 병합 후 통합 테이블 저장
	 * 4. 임베딩 벡터 생성 및 각각 벡터 테이블 저장
	 */
	void scraperStart();
}
