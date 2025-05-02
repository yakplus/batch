package com.likelion.backendplus4.yakplus.drug.application.service.port.in;

/**
 * 의약품 이미지 정보를 수집하는 유스케이스 인터페이스입니다.
 * API를 통해 단일 페이지 또는 전체 이미지 데이터를 수집하는 기능을 정의합니다.
 */
public interface DrugScraperImageUsecase {
	String requestAllData();
}
