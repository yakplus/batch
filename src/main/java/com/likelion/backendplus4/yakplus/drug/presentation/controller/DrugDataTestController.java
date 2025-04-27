package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.DrugDataService;
import com.likelion.backendplus4.yakplus.drug.application.service.scraper.DrugScraper;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.domain.model.port.out.EmbeddingPort;

import lombok.RequiredArgsConstructor;

import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.embedding.EmbeddingModelType;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class DrugDataTestController {
	private final DrugDataService dragDataService;
	private final EmbeddingPort embeddingPort;
	private final DrugScraper scraperUseCase;

	@GetMapping("/data/all")
	public List<Drug> getAllData(Pageable pageable){
		return dragDataService.findAllRawDrug(pageable);
	}

	@GetMapping("/test/embed")
	public ResponseEntity<ApiResponse<float[]>> getEmbedData(){
		log("getEmbedData");
		float[] embedding = embeddingPort.getEmbedding("test", EmbeddingModelType.OPENAI);
		return ApiResponse.success(embedding);

	}

	@GetMapping("/test/parse")
	public ResponseEntity saveAPIData(){
		scraperUseCase.scraperStart();
		return ResponseEntity.ok().build();
	}

}
