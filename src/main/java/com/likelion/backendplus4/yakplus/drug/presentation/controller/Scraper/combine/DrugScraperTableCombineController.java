package com.likelion.backendplus4.yakplus.drug.presentation.controller.Scraper.combine;

import static com.likelion.backendplus4.yakplus.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperDetailUseCase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperTableCombineUsecase;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scraper/combine")
@RequiredArgsConstructor
public class DrugScraperTableCombineController {
	private final DrugScraperTableCombineUsecase drugScraperTableCombineUsecase;

	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start(){
		return success(drugScraperTableCombineUsecase.mergeTable());
	}


}
