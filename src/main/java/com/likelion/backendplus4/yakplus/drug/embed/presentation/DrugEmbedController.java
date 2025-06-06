package com.likelion.backendplus4.yakplus.drug.embed.presentation;

import static com.likelion.backendplus4.yakplus.common.response.ApiResponse.*;

import com.likelion.backendplus4.yakplus.drug.embed.presentation.docs.DrugEmbedControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.embed.application.port.in.DrugEmbedProcessorUseCase;
import com.likelion.backendplus4.yakplus.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * 약품 임베딩 벡터 생성 작업을 제어하는 컨트롤러입니다.
 * 벡터 생성 시작, 중지, 상태 조회 API를 제공합니다.
 *
 * @since 2025-05-02
 */
@RestController
@RequestMapping("/job/embed")
@RequiredArgsConstructor
public class DrugEmbedController implements DrugEmbedControllerDocs {
	private final DrugEmbedProcessorUseCase drugEmbedProcessorUseCase;

	/**
	 * 임베딩 벡터 생성 작업을 시작합니다.
	 *
	 * @return 작업 시작 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start() {
		return success(drugEmbedProcessorUseCase.startEmbedding());
	}

	/**
	 * 임베딩 작업을 중지합니다.
	 *
	 * @return 작업 중지 결과 메시지
	 *
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	@DeleteMapping("/stop")
	public ResponseEntity<ApiResponse<String>> stop() {
		return success(drugEmbedProcessorUseCase.stopEmbedding());
	}

	/**
	 * 임베딩 작업의 상태를 조회합니다.
	 *
	 * @return 작업 상태 메시지
	 * 
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	@GetMapping("/status")
	public ResponseEntity<ApiResponse<String>> status() {
		return success(drugEmbedProcessorUseCase.statusEmbedding());
	}
}
