package com.likelion.backendplus4.yakplus.drug.application.service.scraper;

import org.springframework.stereotype.Service;
import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.drug.application.service.DrugApprovalDetailScraper;
import com.likelion.backendplus4.yakplus.drug.application.service.DrugEmbedProcessor;
import com.likelion.backendplus4.yakplus.drug.application.service.DrugImageGovScraper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugDetailJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DrugScraper {
	DrugImageGovScraper drugImageGovScraper;
	DrugEmbedProcessor embedProcessor;
	DrugApprovalDetailScraper drugApprovalDetailScraper;

	public void scraperStart(){
		drugApprovalDetailScraper.requestUpdateAllRawDataByJdbc();
		drugImageGovScraper.getAllApiData();
	}
}
