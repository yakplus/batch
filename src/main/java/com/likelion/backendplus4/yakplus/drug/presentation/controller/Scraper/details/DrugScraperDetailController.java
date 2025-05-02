package com.likelion.backendplus4.yakplus.drug.presentation.controller.Scraper.details;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperDetailUseCase;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

import static com.likelion.backendplus4.yakplus.response.ApiResponse.*;

import lombok.RequiredArgsConstructor;

@RequestMapping("/scraper/details")
@RequiredArgsConstructor
public class DrugScraperDetailController {
	private final DrugScraperDetailUseCase drugScraperDetailUseCase;

	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start(){
		return success(drugScraperDetailUseCase.requestAllData());
	}


}
