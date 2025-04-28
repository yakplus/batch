package com.likelion.backendplus4.yakplus.drug.application.service.port.in;

/**
 * 의약품 이미지 정보를 수집하는 유스케이스 인터페이스입니다.
 * API를 통해 단일 페이지 또는 전체 이미지 데이터를 수집하는 기능을 정의합니다.
 */
public interface DrugImageScraperUsecase {

	/**
	 * 주어진 페이지 번호에 해당하는 의약품 이미지 데이터를 외부 API로부터 수집하여 저장합니다.
	 *
	 * @param pageNumber 수집할 페이지 번호
	 * @since 2025-04-21
	 */
	void getApiData(int pageNumber);

	/**
	 * 모든 의약품 이미지 정보를 외부 API를 통해 일괄 수집합니다.
	 *
	 * @since 2025-04-21
	 */
	void getAllApiData();
}
