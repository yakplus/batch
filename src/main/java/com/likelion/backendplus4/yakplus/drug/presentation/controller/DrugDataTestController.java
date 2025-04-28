package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
