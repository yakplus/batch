package com.likelion.backendplus4.yakplus.drug.application.service.scraper.combine;

import org.springframework.stereotype.Component;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugScraperTableCombineUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.BatchJobPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DrugScraperTableCombineService implements DrugScraperTableCombineUsecase {
	private final BatchJobPort batchJobPort;

	@Override
	public String mergeTable() {
		return batchJobPort.tableCombineJobStart();
	}
}
