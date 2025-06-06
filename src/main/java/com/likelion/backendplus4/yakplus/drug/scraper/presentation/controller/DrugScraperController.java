package com.likelion.backendplus4.yakplus.drug.scraper.presentation.controller;

import static com.likelion.backendplus4.yakplus.common.response.ApiResponse.*;

import com.likelion.backendplus4.yakplus.drug.scraper.presentation.controller.docs.DrugScraperControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.scraper.application.port.in.DrugScraperUseCase;
import com.likelion.backendplus4.yakplus.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * 의약품 정보 수집 전체 작업을 제어하는 컨트롤러입니다.
 * 작업 시작, 중지, 상태 조회 API를 제공합니다.
 *
 * @since 2025-05-02
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/job/scraper")
public class DrugScraperController implements DrugScraperControllerDocs {
	private final DrugScraperUseCase drugScraperUsecase;

	@Override
	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start() {
		return success(drugScraperUsecase.scraperStart());
	}

	@Override
	@DeleteMapping("/stop")
	public ResponseEntity<ApiResponse<String>> stop() {
		return success(drugScraperUsecase.stop());
	}

	@Override
	@PostMapping("/restart")
	public ResponseEntity<ApiResponse<String>> restart() {
		return success(drugScraperUsecase.restart());
	}

	@Override
	@GetMapping("/status")
	public ResponseEntity<ApiResponse<String>> getBatchProgress() {
		return success(drugScraperUsecase.getStatus());
	}
}
