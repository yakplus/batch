package com.likelion.backendplus4.yakplus.drug.application.service.scraper.combine;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.scraper.DrugScraperTableCombineUseCase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugScraperTableCombineService implements DrugScraperTableCombineUseCase {
	private final BatchJobPort batchJobPort;

	@Override
	public String mergeTable() {
		return batchJobPort.tableCombineJobStart();
	}

	@Override
	public String getStatus() {
		return batchJobPort.tableCombineJobStatus();
	}

	@Override
	public String stop() {
		return batchJobPort.tableCombineJobStop();
	}
}
