package com.likelion.backendplus4.yakplus.symptomdictionary.presentation.controller.docs;

import com.likelion.backendplus4.yakplus.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * 증상 사전 관리 API 문서 정의 인터페이스
 *
 * @since 2025-05-04
 */
@Tag(name = "Dictionary", description = "증상 사전 관리 API")
public interface DictionaryControllerDocs {

    @Operation(
            summary = "증상 사전 데이터 로드 및 저장",
            description = "JSON 파일로부터 증상 사전 데이터를 로드하여 DB 및 Elasticsearch에 저장하는 작업을 수행합니다."
    )
    ResponseEntity<ApiResponse<Void>> setDictionary();
}

