package com.likelion.backendplus4.yakplus.drug.scraper.presentation.controller.docs;

import com.likelion.backendplus4.yakplus.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * 의약품 정보 수집 전체 작업 API 문서 정의 인터페이스
 *
 * @since 2025-05-04
 */
@Tag(name = "Drug Scraper", description = "의약품 정보 수집 전체 작업 API")
public interface DrugScraperControllerDocs {

    @Operation(
            summary = "스크래퍼 작업 시작",
            description = "의약품 정보 수집 전체 작업을 시작합니다."
    )
    ResponseEntity<ApiResponse<String>> start();

    @Operation(
            summary = "스크래퍼 작업 중지",
            description = "진행 중인 의약품 정보 수집 작업을 중지합니다."
    )
    ResponseEntity<ApiResponse<String>> stop();

    @Operation(
            summary = "스크래퍼 작업 재시작",
            description = "중단된 의약품 정보 수집 작업을 재시작합니다."
    )
    ResponseEntity<ApiResponse<String>> restart();

    @Operation(
            summary = "스크래퍼 작업 상태 조회",
            description = "현재 의약품 정보 수집 작업의 상태를 조회합니다."
    )
    ResponseEntity<ApiResponse<String>> getBatchProgress();
}
