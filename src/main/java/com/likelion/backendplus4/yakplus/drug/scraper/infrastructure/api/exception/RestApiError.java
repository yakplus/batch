package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.exception;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * API 호출 시 발생하는 에러 코드
 *
 * @since 2025-04-15
 */
@RequiredArgsConstructor
public enum RestApiError implements ErrorCode {
    PAGE_COUNT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 550001, "API 전체 페이지 개수를 확인하지 못했습니다."),
    ITEM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, 550002, "API 응답에서 적절한 items를 추출하지 못했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public int codeNumber() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
