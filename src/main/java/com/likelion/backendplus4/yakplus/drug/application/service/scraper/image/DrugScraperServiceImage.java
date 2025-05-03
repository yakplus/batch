package com.likelion.backendplus4.yakplus.drug.application.service.scraper.image;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.scraper.DrugScraperImageUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugScraperServiceImage implements DrugScraperImageUsecase {
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
