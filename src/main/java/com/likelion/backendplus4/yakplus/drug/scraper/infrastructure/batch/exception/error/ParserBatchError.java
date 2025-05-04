package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.exception.error;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * ParserBatchError Enum
 * 배치 작업에서 발생할 수 있는 에러 코드들을 정의합니다.
 * 각 에러 코드는 HTTP 상태 코드, 에러 코드, 에러 메시지를 포함합니다.
 *
 * @field httpStatus HTTP 상태 코드
 * @field code 에러 코드
 * @field message 에러 메시지
 * @since 2025-05-02
 */
@RequiredArgsConstructor
public enum ParserBatchError implements ErrorCode {
    ALREADY_RUN(HttpStatus.CONFLICT, 450001, "이미 실행 중인 배치가 있습니다."),
    JOB_RUN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 550001, "JOB 실행 요청은 정상적으로 도달했으나 실행에 실패했습니다."),
    JSON_TYPE_CHANGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 550002, "JSON을 자바 타입으로 변환하는데 실패했습니다.");
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
