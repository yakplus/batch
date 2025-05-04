package com.likelion.backendplus4.yakplus.drug.scraper.application.service;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.scraper.application.port.in.DrugScraperDetailUseCase;
import com.likelion.backendplus4.yakplus.drug.scraper.application.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugScraperDetailService implements DrugScraperDetailUseCase {
	private final BatchJobPort batchJobPort;

	@Override
	public String requestAllData() {
		return batchJobPort.detailScrapJobStart();
	}

	@Override
	public String stop() {
		return batchJobPort.detailScrapJobStop();
	}

	@Override
	public String getStatus() {
		return batchJobPort.detailScrapJobStatus();
	}
}
