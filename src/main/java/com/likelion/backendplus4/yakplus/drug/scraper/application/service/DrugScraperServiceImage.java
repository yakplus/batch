package com.likelion.backendplus4.yakplus.drug.scraper.application.service;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.scraper.application.port.in.DrugScraperImageUseCase;
import com.likelion.backendplus4.yakplus.drug.scraper.application.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugScraperServiceImage implements DrugScraperImageUseCase {
	private final BatchJobPort batchJobPort;

	@Override
	public String requestAllData() {
		return batchJobPort.imageScrapJobStart();
	}

	@Override
	public String stop() {
		return batchJobPort.imageScrapJobStop();
	}

	@Override
	public String getStatus() {
		return batchJobPort.imageScrapJobStatus();
	}
}
