package com.likelion.backendplus4.yakplus.common.batch.infrastructure.adapter;

import com.likelion.backendplus4.yakplus.common.batch.infrastructure.exception.ParserBatchException;
import org.springframework.batch.core.Job;
import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

/**
 * BatchJobPort 인터페이스의 구현체로,
 * Spring Batch Job 객체들을 제어하는 어댑터 클래스입니다.
 * JobManager를 통해 배치 작업의 실행, 중지, 상태 조회 기능을 제공합니다.
 *
 * JobManager: 스프링 배치의 Job을 관리해주는 유틸 클래스
 * Job: 스프링 배치 작업을 정의한 작업 클래스
 *
 * @since 2025-05-02
 */
@Component
@RequiredArgsConstructor
public class BatchJobAdapter implements BatchJobPort {

	private final JobManager jobManager;
	private final Job drugScrapJob;
	private final Job drugDetailScrapJob;
	private final Job drugImageScrapJob;
	private final Job drugTableCombineJob;
	private final Job embedJob;

	/**
	 * 전체 데이터 수집 및 임베딩 작업을 시작합니다.
	 * 이미 실행 중인 작업이 있을 경우 예외를 발생시킵니다.
	 *
	 * @return 작업 시작 결과 메시지
	 * @throws ParserBatchException 중복 실행 예외
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String allJobStart() {
		return jobManager.startJob(drugScrapJob);
	}

	/**
	 * 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 * @@author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String allJobStop() {
		return jobManager.stopRunningBatch(drugScrapJob);
	}

	/**
	 * 중단된 작업을 조회하고, 재개합니다.
	 * 이미 실행 중인 작업이 있을 경우 예외를 발생시킵니다.
	 *
	 * @return 작업 재개 결과 메시지
	 * @throws ParserBatchException 중복 실행 예외
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String allJobResume() {
		return jobManager.restart();
	}

	/**
	 * 전체 작업 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String allJobStatus() {
		return jobManager.getJobStatus(drugScrapJob);
	}

	/**
	 * 의약품 상세 정보 수집 작업을 시작합니다.
	 * 이미 실행 중인 작업이 있을 경우 예외를 발생시킵니다.
	 *
	 * @return 작업 시작 결과 메시지
	 * @throws ParserBatchException 중복 실행 예외
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String detailScrapJobStart() {
		return jobManager.startJob(drugDetailScrapJob);
	}

	/**
	 * 의약품 상세 정보 수집 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String detailScrapJobStop() {
		return jobManager.stopRunningBatch(drugDetailScrapJob);
	}

	/**
	 * 의약품 상세 정보 수집 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String detailScrapJobStatus() {
		return jobManager.getJobStatus(drugDetailScrapJob);
	}

	/**
	 * 의약품 이미지 수집 작업을 시작합니다.
	 * 이미 실행 중인 작업이 있을 경우 예외를 발생시킵니다.
	 *
	 * @return 작업 시작 결과 메시지
	 * @throws ParserBatchException 중복 실행 예외
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String imageScrapJobStart() {
		return jobManager.startJob(drugImageScrapJob);
	}

	/**
	 * 의약품 이미지 수집 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String imageScrapJobStop() {
		return jobManager.stopRunningBatch(drugImageScrapJob);
	}

	/**
	 * 의약품 이미지 수집 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String imageScrapJobStatus() {
		return jobManager.getJobStatus(drugImageScrapJob);
	}

	/**
	 * 의약품 테이블 통합 작업을 시작합니다.
	 * 이미 실행 중인 작업이 있을 경우 예외를 발생시킵니다.
	 *
	 * @return 작업 시작 결과 메시지
	 * @throws ParserBatchException 중복 실행 예외
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String tableCombineJobStart() {
		return jobManager.startJob(drugTableCombineJob);
	}


	/**
	 * 의약품 테이블 통합 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String tableCombineJobStop() {
		return jobManager.stopRunningBatch(drugTableCombineJob);
	}

	/**
	 * 의약품 테이블 통합 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String tableCombineJobStatus() {
		return jobManager.getJobStatus(drugTableCombineJob);
	}

	/**
	 * 증상 텍스트 임베딩 생성 작업을 시작합니다.
	 * 이미 실행 중인 작업이 있을 경우 예외를 발생시킵니다.
	 *
	 * @return 작업 시작 결과 메시지
	 * @throws ParserBatchException 중복 실행 예외
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String embedJobStart(){
		return jobManager.startJob(embedJob);
	}


	/**
	 * 증상 텍스트 임베딩 생성 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String embedJobStop(){
		return jobManager.stopRunningBatch(embedJob);
	}

	/**
	 * 임베딩 생성 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public String embedjobStatus(){
		return jobManager.getJobStatus(embedJob);
	}
}
