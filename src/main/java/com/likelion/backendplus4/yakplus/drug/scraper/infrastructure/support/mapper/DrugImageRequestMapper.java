package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.dto.DrugImageRequest;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugImgEntity;

public class DrugImageRequestMapper {
	public static DrugImgEntity toEntityFromRequest(DrugImageRequest r){
		return DrugImgEntity.builder()
			.drugId(r.getDrugId())
			.productImage(r.getProductImage())
			.pillImage(r.getPillImageUrl())
			.build();
	}
}
