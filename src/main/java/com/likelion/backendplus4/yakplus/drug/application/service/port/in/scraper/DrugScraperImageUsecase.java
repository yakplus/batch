package com.likelion.backendplus4.yakplus.drug.application.service.port.in.scraper;

/**
 * 의약품 이미지 정보를 수집하는 유스케이스 인터페이스입니다.
 *
 * @since 2025-04-21
 */
public interface DrugScraperImageUsecase {

	/**
	 * 의약품 이미지 데이터를 수집하는 배치 작업을 시작합니다.
	 *
	 * @return 작업 시작 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-04-21
	 * @modify 2025-05-02
	 *   - 스프링 배치 형태로 수정
	 */
	String requestAllData();

	/**
	 * 진행 중인 이미지 수집 배치 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 * @since 2025-05-02
	 */
	String stop();

	/**
	 * 이미지 수집 작업의 현재 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @since 2025-05-02
	 */
	String getStatus();
}
