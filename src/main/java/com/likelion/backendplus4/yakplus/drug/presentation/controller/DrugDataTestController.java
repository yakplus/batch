package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugCombineUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugEmbedProcessorUseCase;
import com.likelion.backendplus4.yakplus.drug.application.service.scraper.DrugScraper;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.EmbeddingPort;

import lombok.RequiredArgsConstructor;

import com.likelion.backendplus4.yakplus.drug.infrastructure.embedding.model.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class DrugDataTestController {
	private final DrugScraper scraperUseCase;
	private final DrugCombineUsecase drugCombineUsecase;
	private final DrugEmbedProcessorUseCase drugEmbedProcessorUseCase;

	@GetMapping("/test/parse")
	public ResponseEntity saveAPIData(){
		scraperUseCase.scraperStart();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/test/combine")
	public ResponseEntity saveCombineData(){
		drugCombineUsecase.mergeTable();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/test/embed")
	public ResponseEntity saveEmbedData(){
		drugEmbedProcessorUseCase.startEmbedding();
		return ResponseEntity.ok().build();
	}

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
