package com.likelion.backendplus4.yakplus.drug.index.exception;

import com.likelion.backendplus4.yakplus.common.exception.CustomException;
import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

public class IndexException extends CustomException {
    private final ErrorCode errorCode;

    public IndexException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
