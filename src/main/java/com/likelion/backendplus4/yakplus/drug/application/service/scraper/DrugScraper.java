package com.likelion.backendplus4.yakplus.drug.application.service.scraper;

import org.springframework.stereotype.Service;
import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.drug.application.service.DrugApprovalDetailScraper;
import com.likelion.backendplus4.yakplus.drug.application.service.DrugEmbedProcessor;
import com.likelion.backendplus4.yakplus.drug.application.service.DrugImageGovScraper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugScraper {
	private final DrugImageGovScraper drugImageGovScraper;
	private final DrugEmbedProcessor embedProcessor;
	private final DrugApprovalDetailScraper drugApprovalDetailScraper;
	private final DrugEmbedProcessor drugEmbedProcessor;
	private final GovDrugJpaRepository govDrugJpaRepository;

	public void scraperStart(){
		drugApprovalDetailScraper.requestUpdateAllRawDataByJdbc();
		drugImageGovScraper.getAllApiData();
		drugEmbedProcessor.startEmbedding();
	}
}
