package com.likelion.backendplus4.yakplus.symptomdictionary.exception.error;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DictionaryErrorCode implements ErrorCode {

    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, 11001, "사전 파일 형식이 잘못되었습니다: .json 파일만 허용됩니다"),
    DICTIONARY_LOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, 54001, "증상 사전 로딩에 실패했습니다");

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
