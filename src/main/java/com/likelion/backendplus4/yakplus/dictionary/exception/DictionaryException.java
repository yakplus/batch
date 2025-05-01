package com.likelion.backendplus4.yakplus.dictionary.exception;

import com.likelion.backendplus4.yakplus.common.exception.CustomException;
import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

public class DictionaryException extends CustomException {

    private final ErrorCode errorCode;

    public DictionaryException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
