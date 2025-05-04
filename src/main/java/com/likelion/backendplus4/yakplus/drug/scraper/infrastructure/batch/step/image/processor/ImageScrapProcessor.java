package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.image.processor;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.likelion.backendplus4.yakplus.common.logging.util.LogUtil;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.api.util.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.util.mapper.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.dto.DrugImageRequest;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.support.mapper.DrugImageRequestMapper;

/**
 * pageNumber를 받아 
 * 외부 REST API 호출 → JSON → DTO 리스트 변환 → Entity 리스트로 매핑 
 * 작업을 수행하는 프로세서 클래스입니다.
 * 
 * @field apiRequestManager 외부 API 호출을 위한 매니저
 * @field apiResponseMapper API 응답을 DTO로 변환하는 매퍼
 * @since 2025-05-02
 */
public class ImageScrapProcessor implements ItemProcessor<Integer, List<DrugImgEntity>> {

	private final ApiRequestManager apiRequestManager;
	private final ApiResponseMapper apiResponseMapper;

	public ImageScrapProcessor(ApiRequestManager apiRequestManager,
		ApiResponseMapper apiResponseMapper) {
		this.apiRequestManager = apiRequestManager;
		this.apiResponseMapper = apiResponseMapper;
	}

	/**
	 * 주어진 페이지 번호에 대해 외부 API를 호출하고,
	 * 응답을 DTO 리스트로 변환한 후, Entity 리스트로 매핑합니다.
	 *
	 * @param pageNumber 처리할 페이지 번호
	 * @return DrugImgEntity 리스트
	 * @author 함예정
	 * @since 2025-05-02
	 */
	@Override
	public List<DrugImgEntity> process(Integer pageNumber) {
		LogUtil.log(Thread.currentThread().getName() + " - " + pageNumber + " page 처리 시작");
		String response = apiRequestManager.fetchImageData(pageNumber);
		JsonNode items = apiRequestManager.getItemsFromResponse(response);
		List<DrugImageRequest> drugItems = apiResponseMapper.toListFromDrugImages(items);

        for (DrugImageRequest item : drugItems) {
            String productImage = apiRequestManager.getImage(item.getDrugId());
            if (notEmptyProductImage(productImage)) {
                item.changeProductImageUrl(productImage);
            }
        }

		return drugItems.stream()
			.map(DrugImageRequestMapper::toEntityFromRequest)
			.toList();
	}

	/**
	 * 주어진 productImage가 null이 아니고 길이가 10보다 큰지 확인합니다.
	 *
	 * @param productImage 검사할 이미지 URL
	 * @return true: 유효한 이미지 URL, false: 유효하지 않은 이미지 URL
	 * @author 함예정
	 * @since 2025-05-02
	 */
	private boolean notEmptyProductImage(String productImage) {
		return productImage != null && productImage.length() > 10;
	}
}
