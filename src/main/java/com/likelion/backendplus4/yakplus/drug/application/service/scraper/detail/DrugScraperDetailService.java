package com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.scraper.DrugScraperDetailUseCase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;

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
