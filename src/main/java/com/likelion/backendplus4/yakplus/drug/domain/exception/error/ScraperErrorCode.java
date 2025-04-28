package com.likelion.backendplus4.yakplus.drug.domain.exception.error;

import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ScraperErrorCode implements ErrorCode {

    DB_ERROR_PERMIT_INFO(HttpStatus.INTERNAL_SERVER_ERROR, 300001, "허가 정보를 조회하는데 실패했습니다."),
    DB_ERROR_IMAGE_INFO(HttpStatus.INTERNAL_SERVER_ERROR, 300002, "이미지 정보를 조회하는데 실패했습니다."),
    DB_ERROR_COMBINED_INFO(HttpStatus.INTERNAL_SERVER_ERROR, 300003, "결합된 정보를 조회하는데 실패했습니다."),
    API_CONNECT_FAIL(HttpStatus.BAD_GATEWAY, 400001, "외부 API 연결에 실패했습니다."),
    API_DRUG_DETAIL_PARSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 300004, "허가 정보 API에서 정보 파싱에 실패했습니다."),
    MATERIAL_PARSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,300006, "성분 파싱에 실패 했습니다"),
    RESPONSE_TYPE_CHANGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 300005, "API 응답을 객체 타입으로 변환하는데 실패했습니다."),
    PARSING_ERROR(HttpStatus.BAD_REQUEST, 400001, "데이터 파싱에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public int codeNumber() {
        return 0;
    }

    @Override
    public String message() {
        return "";
    }
}
