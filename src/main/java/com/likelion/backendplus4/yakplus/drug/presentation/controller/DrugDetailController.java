package com.likelion.backendplus4.yakplus.drug.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugDetailScraperUsecase;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DrugDetailController {
	private final DrugDetailScraperUsecase drugDetailScraperUsecase;

	@PostMapping("/gov/api/parser/detail/start")
	public ResponseEntity saveAPIData(){
		drugDetailScraperUsecase.requestSingleData();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/gov/api/parser/detail/startAll")
	public ResponseEntity saveAPIDataAll(){
		drugDetailScraperUsecase.requestAllData();
		return ResponseEntity.ok().build();
	}
}
