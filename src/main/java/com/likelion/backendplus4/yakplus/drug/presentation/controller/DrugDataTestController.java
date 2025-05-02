package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperTableCombineUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugEmbedProcessorUseCase;

import lombok.RequiredArgsConstructor;

import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;

@RestController
@RequiredArgsConstructor
public class DrugDataTestController {
	private final DrugScraperTableCombineUsecase drugScraperTableCombineUsecase;
	private final DrugEmbedProcessorUseCase drugEmbedProcessorUseCase;

	// 임베딩 모델을 스위칭하는 엔드포인트
	@GetMapping("/test/switchEmbed")
	public ResponseEntity switchEmbedding(@RequestParam String modelType) {
		drugEmbedProcessorUseCase.switchEmbeddingModel(modelType);
		return ResponseEntity.ok().build();
	}

	// 현재 사용 중인 임베딩 모델을 조회하는 엔드포인트
	@GetMapping("/test/currentEmbed")
	public ResponseEntity getCurrentEmbedding() {
		EmbeddingModelType currentModel = drugEmbedProcessorUseCase.getCurrentEmbeddingModel();
		return ResponseEntity.ok(currentModel);  // 현재 모델을 반환
	}
}
