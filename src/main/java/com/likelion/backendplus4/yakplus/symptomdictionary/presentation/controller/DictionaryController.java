package com.likelion.backendplus4.yakplus.symptomdictionary.presentation.controller;

import com.likelion.backendplus4.yakplus.symptomdictionary.application.port.in.DictionaryUseCase;
import com.likelion.backendplus4.yakplus.common.response.ApiResponse;
import com.likelion.backendplus4.yakplus.symptomdictionary.presentation.controller.docs.DictionaryControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;
import static com.likelion.backendplus4.yakplus.common.response.ApiResponse.*;

/**
 * 사전 관리용 REST API를 제공하는 컨트롤러 클래스입니다.
 *
 * @modified 2025-05-01
 * @since 2025-05-01
 */
@RestController
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class DictionaryController implements DictionaryControllerDocs {

    private final DictionaryUseCase dictionaryUseCase;

    /**
     * 증상 사전 데이터를 JSON 파일로부터 로드하여
     * DB 및 Elasticsearch에 저장하는 작업을 수행합니다.
     * <p>
     * 1) JSON 로더를 통해 증상 리스트 로드
     * 2) JPA 어댑터로 DB 저장
     * 3) Elasticsearch 어댑터로 색인 저장
     *
     * @return 성공 여부를 담은 ApiResponse<Void>
     * @throws RuntimeException 처리 중 오류 발생 시 전달
     * @author 박찬병
     * @modified 2025-05-01
     * @since 2025-05-01
     */
    @PostMapping("/set")
    public ResponseEntity<ApiResponse<Void>> setDictionary() {
        log("setDictionary() 호출");
        dictionaryUseCase.setDictionary();

        return success();
    }

}
