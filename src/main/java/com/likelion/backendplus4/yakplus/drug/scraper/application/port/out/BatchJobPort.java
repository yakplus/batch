package com.likelion.backendplus4.yakplus.drug.scraper.application.port.out;

/**
 * 의약품 데이터 수집 및 임베딩과 관련된 배치 작업을 제어하기 위한 포트 인터페이스입니다.
 * 각 배치 작업의 시작, 중지, 상태 조회 기능을 정의합니다.
 *
 * @since 2025-05-02
 */
public interface BatchJobPort {

	/**
	 * 전체 배치 작업을 순차적으로 시작합니다.
	 *
	 * @return 전체 작업 시작 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String allJobStart();

	/**
	 * 전체 배치 작업을 중지합니다.
	 *
	 * @return 전체 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String allJobStop();

	/**
	 * 중지된 전체 배치 작업을 재개합니다.
	 *
	 * @return 전체 작업 재개 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String allJobResume();


	/**
	 * 전체 배치 작업의 상태를 조회합니다.
	 *
	 * @return 전체 작업 상태 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String allJobStatus();

	/**
	 * 상세 정보 스크래핑 작업을 시작합니다.
	 *
	 * @return 작업 시작 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String detailScrapJobStart();

	/**
	 * 상세 정보 스크래핑 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String detailScrapJobStop();

	/**
	 * 상세 정보 스크래핑 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String detailScrapJobStatus();

	/**
	 * 이미지 스크래핑 작업을 시작합니다.
	 *
	 * @return 작업 시작 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String imageScrapJobStart();

	/**
	 * 이미지 스크래핑 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String imageScrapJobStop();

	/**
	 * 이미지 스크래핑 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String imageScrapJobStatus();

	/**
	 * 상세 정보와 이미지 데이터를 병합하는 작업을 시작합니다.
	 *
	 * @return 작업 시작 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String tableCombineJobStart();

	/**
	 * 테이블 병합 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String tableCombineJobStop();

	/**
	 * 테이블 병합 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String tableCombineJobStatus();

	/**
	 * 임베딩 작업을 시작합니다.
	 *
	 * @return 작업 시작 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String embedJobStart();

	/**
	 * 임베딩 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String embedJobStop();

	/**
	 * 임베딩 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * 
	 * @author 함예정
	 * @since 2025-05-02
	 */
	String embedjobStatus();
}
