package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.domain.model.GovDrugDetail;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.ApiDataDrugImgEntity;

public class DrugImageMapper {
	public static DrugImage toDomainFromEntity(ApiDataDrugImgEntity e){
		return DrugImage.builder()
			.drugId(e.getDrugId())
			.imageUrl(e.getImgUrl())
			.build();
	}
}
