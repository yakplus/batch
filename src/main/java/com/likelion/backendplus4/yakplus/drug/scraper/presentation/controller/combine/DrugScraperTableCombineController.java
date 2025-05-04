package com.likelion.backendplus4.yakplus.drug.scraper.presentation.controller.combine;

import static com.likelion.backendplus4.yakplus.common.response.ApiResponse.*;

import com.likelion.backendplus4.yakplus.drug.scraper.presentation.controller.docs.DrugScraperTableCombineControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.scraper.application.port.in.DrugScraperTableCombineUseCase;
import com.likelion.backendplus4.yakplus.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * 의약품 상세정보와 이미지 테이블 병합 작업을 제어하는 컨트롤러입니다.
 * 작업 시작, 중지, 상태 조회 API를 제공합니다.
 *
 * @since 2025-05-02
 */
@RestController
@RequestMapping("/scraper/combine")
@RequiredArgsConstructor
public class DrugScraperTableCombineController implements DrugScraperTableCombineControllerDocs {
	private final DrugScraperTableCombineUseCase drugScraperTableCombineUsecase;

	/**
	 * 의약품 상세정보와 이미지 정보 테이블을 병합하는 작업을 시작합니다.
	 *
	 * @return 성공 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start(){
		return success(drugScraperTableCombineUsecase.mergeTable());
	}

	/**
	 * 진행 중인 작업을 중지합니다.
	 *
	 * @return 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	@DeleteMapping("/stop")
	public ResponseEntity<ApiResponse<String>> stop() {
		return success(drugScraperTableCombineUsecase.stop());
	}

	/**
	 * 작업의 현재 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	@GetMapping("/status")
	public ResponseEntity<ApiResponse<String>> getBatchProgress() {
		return success(drugScraperTableCombineUsecase.getStatus());
	}
}
