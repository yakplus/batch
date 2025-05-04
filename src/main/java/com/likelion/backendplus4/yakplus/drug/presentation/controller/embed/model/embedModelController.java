package com.likelion.backendplus4.yakplus.drug.presentation.controller.embed.model;

import static com.likelion.backendplus4.yakplus.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.embed.DrugEmbedProcessorUseCase;
import com.likelion.backendplus4.yakplus.drug.presentation.controller.embed.dto.ModelSwitchRequeset;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * 임베딩 모델 전환 및 현재 모델 조회를 위한 컨트롤러입니다.
 *
 * @since 2025-05-02
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/embed/model")
public class embedModelController {
	private final DrugEmbedProcessorUseCase drugEmbedProcessorUseCase;

	/**
	 * 임베딩 모델을 전환합니다.
	 *
	 * @param request 전환할 모델 타입 요청 DTO
	 * @return 전환된 모델 정보 응답
	 *
	 * @author 정안식
	 * @since 2025-05-01
	 * @modify 2025-05-02 함예정
	 *   - 스프링 배치 전환에 따른 수정
	 */
	@PostMapping("/switch")
	public ResponseEntity<ApiResponse<String>> switchEmbedding(@RequestBody ModelSwitchRequeset request) {
		drugEmbedProcessorUseCase.switchEmbeddingModel(request.getModelType());
		return success("요청 성공: "+ drugEmbedProcessorUseCase.getCurrentEmbeddingModel());
	}

	/**
	 * 현재 사용 중인 임베딩 모델을 조회합니다.
	 *
	 * @return 현재 모델 이름
	 *
	 * @author 정안식
	 * @since 2025-05-01
	 * @modify 2025-05-02 함예정
	 *   - 스프링 배치 전환에 따른 수정
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<String>> getModel(){
		return success(drugEmbedProcessorUseCase.getCurrentEmbeddingModel());
	}
}
