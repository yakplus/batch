package com.likelion.backendplus4.yakplus.drug.application.service.scraper;

import org.springframework.stereotype.Service;
import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugDetailScraperUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.scraper.embed.DrugEmbedProcessor;
import com.likelion.backendplus4.yakplus.drug.application.service.scraper.image.DrugImageGovScraper;

import lombok.RequiredArgsConstructor;

/**
 * @class DrugScraper
 * @description
 * 약품 관련 데이터 수집을 담당하는 서비스 클래스
 * 1) 약품 상세 정보
 * 2) 약품 이미지
 * 위 항목을 순차적으로 수집하고 전처리 작업을 거쳐 하나의 테이블로 저장한다.
 * 약품 효능 값을 임베딩하여 각 임베딩 모델 별로 저장한다.
 *
 * @since 2025-04-21
 */
@Service
@RequiredArgsConstructor
public class DrugScraper {
	private final DrugImageGovScraper drugImageGovScraper;
	private final DrugDetailScraperUsecase drugDetailScraperUsecase;
	private final DrugEmbedProcessor drugEmbedProcessor;

	public void scraperStart(){
		drugDetailScraperUsecase.requestAllData();
		drugImageGovScraper.getAllApiData();
		drugEmbedProcessor.startEmbedding();
	}
}
