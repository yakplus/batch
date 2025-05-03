package com.likelion.backendplus4.yakplus.drug.application.service.scraper;

import org.springframework.stereotype.Service;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.scraper.DrugScraperUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugScraperService implements DrugScraperUsecase {
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
