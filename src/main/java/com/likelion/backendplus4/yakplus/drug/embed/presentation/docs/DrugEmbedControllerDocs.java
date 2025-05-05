package com.likelion.backendplus4.yakplus.drug.embed.presentation.docs;

import org.springframework.http.ResponseEntity;

import com.likelion.backendplus4.yakplus.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 약품 임베딩 벡터 생성 작업 API 문서 정의 인터페이스
 *
 * @since 2025-05-04
 */
@Tag(name = "Drug Embed", description = "약품 임베딩 벡터 생성 작업 API")
public interface DrugEmbedControllerDocs {

    @Operation(summary = "임베딩 벡터 생성 작업 시작", description = "임베딩 벡터 생성 작업을 시작합니다.")
    ResponseEntity<ApiResponse<String>> start();

    @Operation(summary = "임베딩 작업 중지", description = "진행 중인 임베딩 작업을 중지합니다.")
    ResponseEntity<ApiResponse<String>> stop();

    @Operation(summary = "임베딩 작업 상태 조회", description = "현재 임베딩 작업의 상태를 조회합니다.")
    ResponseEntity<ApiResponse<String>> status();
}
