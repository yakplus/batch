package com.likelion.backendplus4.yakplus.drug.index.presentation.controller.docs;

import com.likelion.backendplus4.yakplus.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * 약품 인덱싱 API 문서 정의 인터페이스
 *
 * @since 2025-05-04
 */
@Tag(name = "Drug Indexing", description = "약품 인덱싱 API")
public interface DrugIndexingControllerDocs {

    @Operation(
            summary = "자연어 인덱싱 작업 실행",
            description = "약품 인덱싱 작업을 시작합니다."
    )
    void index();

    @Operation(
            summary = "키워드 인덱싱 작업 실행",
            description = "DB로부터 조회한 약품 데이터를 Elasticsearch에 일괄 색인합니다."
    )
    ResponseEntity<ApiResponse<Void>> triggerIndex();
}
