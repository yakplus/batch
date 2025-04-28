package com.likelion.backendplus4.yakplus.drug.application.service.scraper.image;

import java.util.List;
import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.application.service.port.in.DrugImageScraperUsecase;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.ApiRequestPort;
import com.likelion.backendplus4.yakplus.drug.application.service.port.out.DrugImageRepositoryPort;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.domain.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.domain.exception.error.ScraperErrorCode;

import lombok.RequiredArgsConstructor;

/**
 * 정부 API로부터 의약품 이미지 정보를 수집하여 저장하는 서비스 클래스입니다.
 * {@link DrugImageScraperUsecase}를 구현하며,
 * 단일 페이지 또는 전체 페이지의 이미지 데이터를 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class DrugImageGovScraper implements DrugImageScraperUsecase {
	private final ApiRequestPort apiRequestPort;
	private final DrugImageRepositoryPort drugImageRepositoryPort;
	private final ObjectMapper objectMapper;

	@Override
	public void getApiData(int pageNumber){
		JsonNode items = apiRequestPort.getAllImageData(pageNumber);
		List<DrugImage> imgData = changeTypeToList(items);
		drugImageRepositoryPort.saveAllAndFlush(imgData);
	}

	@Override
	public void getAllApiData(){
		int totalPageCount = apiRequestPort.getImageTotalPageCount();
		for(int i=1;i<=totalPageCount;i++){
			JsonNode items = apiRequestPort.getAllImageData(i);
			List<DrugImage> imgData = null;
			try {
				imgData = objectMapper.readValue(items.toString(),
					new TypeReference<List<DrugImage>>() {});
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			drugImageRepositoryPort.saveAllAndFlush(imgData);
		}
	}

	/**
	 * JsonNode 형태의 데이터를 {@link DrugImage} 리스트로 변환합니다.
	 * 변환 실패 시 {@link ScraperException}을 발생시킵니다.
	 *
	 * @param items 변환할 JSON 데이터
	 * @return {@link DrugImage} 리스트
	 */
	private List<DrugImage> changeTypeToList(JsonNode items) {
		List<DrugImage> imgData;
		try {
			imgData = objectMapper.readValue(items.toString(),
				new TypeReference<List<DrugImage>>() {});
		} catch (JsonProcessingException e) {
			throw new ScraperException(ScraperErrorCode.RESPONSE_TYPE_CHANGE_FAIL);
		}
		return imgData;
	}
}
