package com.likelion.backendplus4.yakplus.temp.presentation.controller;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.response.ApiResponse;
import com.likelion.backendplus4.yakplus.temp.application.port.in.IndexTempUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drugs/index")
@RequiredArgsConstructor
public class DrugTempController {

    private final IndexTempUseCase indexUseCase;

    /**
     * 색인 배치 작업을 실행하여, DB로부터 조회한 약품 증상 데이터를 Elasticsearch에 일괄 색인합니다.
     *
     * @return 색인 작업 성공 여부 응답 (Void)
     * @author 박찬병
     * @modified 2025-04-25
     * @since 2025-04-24
     */
    @PostMapping("/keyword")
    public ResponseEntity<ApiResponse<Void>> triggerIndex() {
        log("indexSymptom 요청 수신");
        indexUseCase.indexKeyword();
        return ApiResponse.success();
    }

}
