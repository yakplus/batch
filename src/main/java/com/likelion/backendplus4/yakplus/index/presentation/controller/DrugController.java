package com.likelion.backendplus4.yakplus.index.presentation.controller;

import com.likelion.backendplus4.yakplus.index.application.port.in.IndexUseCase;
import com.likelion.backendplus4.yakplus.index.presentation.controller.dto.request.IndexRequest;
import com.likelion.backendplus4.yakplus.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 약품 인덱싱 API 엔드포인트를 제공하는 컨트롤러 클래스
 *
 * @modified 2025-04-25
 * @since 2025-04-22
 */
@RestController
@RequestMapping("/api/drugs/index")
@RequiredArgsConstructor
public class DrugController {
    private final IndexUseCase indexUseCase;

    /**
     * 색인 생성 요청을 처리한다.
     *
     * @param request 인덱싱 범위 및 개수 정보를 담은 요청 객체
     * @author 정안식
     * @modified 2025-04-24
     * @since 2025-04-22
     */
    @PostMapping("/save")
    public void index(@RequestBody IndexRequest request) {
        log("index 컨트롤러 요청 수신" + request.toString());
        indexUseCase.index(request);
    }

    /**
     * 색인 배치 작업을 실행하여, DB로부터 조회한 약품 증상 데이터를 Elasticsearch에 일괄 색인합니다.
     *
     * @return 색인 작업 성공 여부 응답 (Void)
     * @author 박찬병
     * @since 2025-04-24
     * @modified 2025-04-25
     */
    @PostMapping("/symptom")
    public ResponseEntity<ApiResponse<Void>> triggerIndex() {
        log("indexSymptom 요청 수신");
        indexUseCase.indexSymptom();
        return ApiResponse.success();
    }
}