package com.likelion.backendplus4.yakplus.index.exception.error;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum IndexErrorCode implements ErrorCode {
    RAW_DATA_FETCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 430001, "원시 데이터 조회 실패"),
    ES_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 430002, "Elasticsearch 저장 실패"),
    EMBEDDING_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 440001, "임베딩 API 호출 실패");

    private final HttpStatus status;
    private final int code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return status;
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
