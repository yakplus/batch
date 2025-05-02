package com.likelion.backendplus4.yakplus.drug.presentation.controller.Scraper.image;

import static com.likelion.backendplus4.yakplus.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperImageUsecase;
import com.likelion.backendplus4.yakplus.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/scraper/details")
@RequiredArgsConstructor
public class DrugScraperImageController {
	private final DrugScraperImageUsecase drugScraperImageUsecase;

	@PostMapping("/start")
	public ResponseEntity<ApiResponse<String>> start(){
		return success(drugScraperImageUsecase.requestAllData());
	}


}
