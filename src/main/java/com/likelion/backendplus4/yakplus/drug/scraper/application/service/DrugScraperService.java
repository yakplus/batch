package com.likelion.backendplus4.yakplus.drug.scraper.application.service;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.scraper.application.port.in.DrugScraperUseCase;
import com.likelion.backendplus4.yakplus.drug.scraper.application.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugScraperService implements DrugScraperUseCase {
	private final BatchJobPort batchJobPort;

	@Override
	public String scraperStart() {
		return batchJobPort.allJobStart();
	}

	@Override
	public String stop() {
		return batchJobPort.allJobStop();
	}

	@Override
	public String restart() {
		return batchJobPort.allJobResume();
	}

	@Override
	public String getStatus() {
		return batchJobPort.allJobStatus();
	}
}
