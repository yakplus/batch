package com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperDetailUseCase;
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
}
