package com.likelion.backendplus4.yakplus.common.exception.handler;

import static com.likelion.backendplus4.yakplus.common.logging.util.LogUtil.log;

import com.likelion.backendplus4.yakplus.common.exception.CustomException;
import com.likelion.backendplus4.yakplus.common.exception.error.ErrorCode;
import com.likelion.backendplus4.yakplus.common.logging.util.LogLevel;
import com.likelion.backendplus4.yakplus.common.response.ApiResponse;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 클래스
 * 컨트롤러에서 발생한 예외를 공통적으로 처리한다.
 *
 * @modified 2025-05-03
 * @since 2025-04-16
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 에러 코드 상수 정의 (정수형 코드 사용)
    private static final int ILLEGAL_ARGUMENT_CODE = 300000;
    private static final int METHOD_ARGUMENT_NOT_VALID_CODE = 300001;
    private static final int BIND_EXCEPTION_CODE = 300002;
    private static final int INTERNAL_SERVER_ERROR_CODE = 500000;

    /**
     * CustomException 처리
     * ErrorCode 인터페이스 기반으로 확장 가능한 방식으로 처리한다.
     *
     * @param ex CustomException 객체
     * @return 에러 응답
     * @author 정안식
     * @modified 2025-05-03 박찬병
     * @since 2025-04-16
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return buildErrorResponse(
                errorCode.httpStatus(),
                errorCode.codeNumber(),
                errorCode.message(),
                ex
        );
    }

    /**
     * IllegalArgumentException 처리
     * 잘못된 파라미터에 대한 예외 응답 처리
     *
     * @param ex 예외 객체
     * @return 에러 응답
     * @author 정안식
     * @modified 2025-05-03 박찬병
     * @since 2025-04-16
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ILLEGAL_ARGUMENT_CODE,
                ex.getMessage(),
                ex
        );
    }

    /**
     * MethodArgumentNotValidException 처리
     * 유효성 검사 실패에 대한 응답 처리
     *
     * @param ex 예외 객체
     * @return 에러 응답
     * @author 정안식
     * @modified 2025-05-03 박찬병
     * @since 2025-04-16
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = getErrorMessage(ex);
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                METHOD_ARGUMENT_NOT_VALID_CODE,
                errorMessage,
                ex
        );
    }

    /**
     * BindException 처리
     * GET 요청 파라미터나 폼 바인딩 유효성 실패 시 처리
     *
     * @param ex BindException 오류
     * @return 에러 응답
     * @author 박찬병
     * @modified 2025-05-03 박찬병
     * @since 2025-04-17
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException ex) {
        String errorMessage = getErrorMessage(ex);
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                BIND_EXCEPTION_CODE,
                errorMessage,
                ex
        );
    }

    /**
     * 기타 모든 예외 처리
     * 정의되지 않은 예외는 내부 서버 오류로 응답
     *
     * @param ex 예외 객체
     * @return 에러 응답
     * @author 정안식
     * @modified 2025-05-03 박찬병
     * @since 2025-04-16
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR_CODE,
                "내부 서버 오류",
                ex
        );
    }

    /**
     * 공통 에러 응답 생성 메서드
     * 예외 로깅 후 ApiResponse.error를 통해 표준화된 에러 응답을 생성한다.
     *
     * @param status    HTTP 상태 코드
     * @param errorCode 에러 코드 (정수형)
     * @param message   에러 메시지
     * @param ex        발생한 예외 객체
     * @return ResponseEntity<ApiResponse < Void>> 형태의 에러 응답
     * @author 박찬병
     * @modified 2025-05-03 박찬병
     * @since 2025-04-18
     */
    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(
            HttpStatus status,
            int errorCode,
            String message,
            Throwable ex
    ) {
        log(LogLevel.ERROR, ex.getClass().getSimpleName() + ": " + ex.getMessage(), ex);
        return ApiResponse.error(status, String.valueOf(errorCode), message);
    }

    /**
     * BindingResult 분석 후 필드별 오류 메시지 조합
     *
     * @param ex BindException 또는 MethodArgumentNotValidException 객체
     * @return 필드명과 메시지를 콤마로 연결한 오류 문자열
     * @author 박찬병
     * @modified 2025-05-03 박찬병
     * @since 2025-04-16
     */
    private static String getErrorMessage(BindException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}