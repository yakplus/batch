package com.likelion.backendplus4.yakplus.drug.scraper.presentation.controller.docs;

import com.likelion.backendplus4.yakplus.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * 의약품 정보 수집 전체 작업 API 문서 정의 인터페이스
 *
 * 이 API는 의약품 상세정보 수집, 이미지 정보 수집, 테이블 병합, 임베딩 벡터 생성 등
 * 전체 스크래핑 파이프라인을 제어하기 위한 엔드포인트를 제공합니다.
 *
 * @since 2025-05-04
 */
@Tag(name = "Drug Scraper", description = "의약품 정보 수집 전체 작업을 제어하는 API")
public interface DrugScraperControllerDocs {

    @Operation(
            summary = "전체 스크래핑 작업 시작",
            description = "의약품 상세정보 수집, 이미지 수집, 병합, 임베딩 벡터 생성을 포함한 전체 스크래핑 파이프라인을 순차적으로 실행합니다. " +
                    "작업이 정상적으로 시작되면 작업 ID 또는 시작 메시지를 반환합니다."
    )
    ResponseEntity<ApiResponse<String>> start();

    @Operation(
            summary = "진행 중인 스크래핑 작업 중지",
            description = "현재 실행 중인 전체 스크래핑 파이프라인 작업을 안전하게 중단합니다. " +
                    "중단된 상태는 유지되어 재시작 시 중단된 지점부터 재개할 수 있습니다."
    )
    ResponseEntity<ApiResponse<String>> stop();

    @Operation(
            summary = "스크래핑 작업 재시작",
            description = "중단된 스크래핑 작업을 중단 시점부터 재개하여 다시 실행합니다. " +
                    "restart 엔드포인트 호출 시 이전 작업 상태를 기반으로 이어서 수행합니다."
    )
    ResponseEntity<ApiResponse<String>> restart();

    @Operation(
            summary = "스크래핑 작업 상태 조회",
            description = "현재 전체 스크래핑 작업의 진행 상태(단계, 완료 비율, 오류 여부 등)를 조회하여 반환합니다."
    )
    ResponseEntity<ApiResponse<String>> getBatchProgress();
}
