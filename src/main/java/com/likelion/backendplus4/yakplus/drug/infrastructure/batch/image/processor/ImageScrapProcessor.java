package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.image.processor;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.likelion.backendplus4.yakplus.common.util.log.LogUtil;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.api.ApiRequestManager;
import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.mapper.ApiResponseMapper;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugImageRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.ApiDataDrugImgEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper.DrugImageRequestMapper;

/**
 * pageNumber를 받아 외부 REST API 호출 → JSON → DTO 리스트 변환 →
 * Entity 리스트로 매핑
 */
public class ImageScrapProcessor implements ItemProcessor<Integer, List<ApiDataDrugImgEntity>> {

	private final ApiRequestManager apiRequestManager;
	private final ApiResponseMapper apiResponseMapper;

	public ImageScrapProcessor(ApiRequestManager apiRequestManager,
		ApiResponseMapper apiResponseMapper) {
		this.apiRequestManager = apiRequestManager;
		this.apiResponseMapper = apiResponseMapper;
	}

	@Override
	public List<ApiDataDrugImgEntity> process(Integer pageNumber) throws Exception {
		LogUtil.log(Thread.currentThread().getName() + " - " + pageNumber + " page 처리 시작");
		String response = apiRequestManager.fetchImageData(pageNumber);
		JsonNode items = apiRequestManager.getItemsFromResponse(response);
		List<DrugImageRequest> drugItems = apiResponseMapper.toListFromDrugImages(items);

		for (int i = 0; i < drugItems.size(); i++) {
			DrugImageRequest item = drugItems.get(i);
			String productImage = apiRequestManager.getImage(item.getDrugId());
			if (productImage != null && productImage.length() > 10) {
				item.changeProductImageUrl(productImage);
			}
		}

		return drugItems.stream()
			.map(DrugImageRequestMapper::toEntityFromRequest)
			.toList();
	}
}
