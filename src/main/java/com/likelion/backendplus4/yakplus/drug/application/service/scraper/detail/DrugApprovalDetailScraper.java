package com.likelion.backendplus4.yakplus.drug.application.service.scraper.detail;

public interface DrugApprovalDetailScraper {
	void requestUpdateRawData();

	void requestUpdateAllRawData();

	void requestUpdateAllRawDataByJdbc();
}
