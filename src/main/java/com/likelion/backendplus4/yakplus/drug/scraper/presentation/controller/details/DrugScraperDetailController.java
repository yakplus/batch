package com.likelion.backendplus4.yakplus.drug.scraper.presentation.controller.details;

import static com.likelion.backendplus4.yakplus.common.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.scraper.application.port.in.DrugScraperDetailUseCase;
import com.likelion.backendplus4.yakplus.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * 의약품 상세정보 수집 작업을 제어하는 컨트롤러입니다.
 * 작업 시작, 중지, 상태 조회 API를 제공합니다.
 *
 * @since 2025-05-02
 */
@RestController
@RequestMapping("/scraper/details")
@RequiredArgsConstructor
public class DrugScraperDetailController {
	private final DrugScraperDetailUseCase drugScraperDetailUseCase;

	/**
	 * 의약품 상세정보 수집 작업을 시작합니다.
	 *
	 * @return 성공 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start() {
		return success(drugScraperDetailUseCase.requestAllData());
	}

	/**
	 * 의약품 상세정보 수집 작업을 중지합니다.
	 *
	 * @return 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@DeleteMapping("/stop")
	public ResponseEntity<ApiResponse<String>> stop() {
		return success(drugScraperDetailUseCase.stop());
	}

	/**
	 * 작업의 현재 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@GetMapping("/status")
	public ResponseEntity<ApiResponse<String>> getBatchProgress() {
		return success(drugScraperDetailUseCase.getStatus());
	}
}
