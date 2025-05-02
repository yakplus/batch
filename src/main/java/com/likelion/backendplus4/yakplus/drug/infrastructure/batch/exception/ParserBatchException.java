package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception;

import com.likelion.backendplus4.yakplus.common.exception.CustomException;
import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

public class ParserBatchException extends CustomException {
	private final ErrorCode errorCode;

	public ParserBatchException(ErrorCode errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}