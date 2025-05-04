package com.likelion.backendplus4.yakplus.drug.embed.presentation.docs;

import com.likelion.backendplus4.yakplus.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * Embedding 라우팅 어댑터 API 문서 정의 인터페이스
 *
 * @since 2025-05-04
 */
@Tag(name = "Embedding Router", description = "Embedding 라우팅 어댑터 전환 및 조회 API")
public interface EmbeddingRouterControllerDocs {

    @Operation(
            summary = "Embedding adapter 전환",
            description = "지정된 adapterBeanName에 해당하는 embedding adapter로 전환합니다."
    )
    ResponseEntity<ApiResponse<String>> switchAdapter(
            @Parameter(
                    name = "adapterBeanName",
                    description = "전환할 adapter Bean 이름",
                    in = ParameterIn.PATH,
                    required = true
            )
            String adapterBeanName
    );

    @Operation(
            summary = "현재 활성화된 adapter 조회",
            description = "현재 사용 중인 embedding adapter Bean 이름을 반환합니다."
    )
    ResponseEntity<ApiResponse<String>> checkCurrentAdapter();
}
