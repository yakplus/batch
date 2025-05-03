package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParserBatchError implements ErrorCode {
	ALREADY_RUN(HttpStatus.CONFLICT, 400001, "이미 실행 중인 배치가 있습니다."),
	JOB_RUN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 500000, "JOB 실행 요청은 정상적으로 도달했으나 실행에 실패했습니다."),
	JSON_TYPE_CHANGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 500001, "JSON을 자바 타입으로 변환하는데 실패했습니다.");
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
