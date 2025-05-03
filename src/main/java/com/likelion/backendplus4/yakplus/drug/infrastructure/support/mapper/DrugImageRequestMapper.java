package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto.DrugImageRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.ApiDataDrugImgEntity;

public class DrugImageRequestMapper {
	public static ApiDataDrugImgEntity toEntityFromRequest(DrugImageRequest r){
		return ApiDataDrugImgEntity.builder()
			.drugId(r.getDrugId())
			.productImage(r.getProductImage())
			.pillImage(r.getPillImageUrl())
			.build();
	}
}
