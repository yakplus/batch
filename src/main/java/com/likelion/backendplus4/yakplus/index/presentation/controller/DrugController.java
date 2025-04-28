package com.likelion.backendplus4.yakplus.index.presentation.controller;

import com.likelion.backendplus4.yakplus.index.application.port.in.IndexUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

/**
 * 약품 인덱싱 API 엔드포인트를 제공하는 컨트롤러 클래스
 *
 * @modified 2025-04-28
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
     * @author 정안식
     * @modified 2025-04-28
     * 25.04.28 - IndexUseRequest를 인자에서 제거하였습니다.(추후 임베딩 모델 선택 로직 추가시 변경예정)
     * @since 2025-04-22
     */
    @PostMapping("/save")
    public void index() {
        log("컨트롤러 indexAll 요청 수신");
        indexUseCase.index();
    }
}