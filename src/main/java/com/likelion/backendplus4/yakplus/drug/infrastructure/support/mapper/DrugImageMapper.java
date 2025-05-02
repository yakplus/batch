package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import java.util.List;

import com.likelion.backendplus4.yakplus.drug.domain.model.DrugImage;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.ApiDataDrugImgEntity;

public class DrugImageMapper {
	public static DrugImage toDomainFromEntity(ApiDataDrugImgEntity e){
		return DrugImage.builder()
			.drugId(e.getDrugId())
			.imageUrl(e.getPillImage())
			.build();
	}

	public static ApiDataDrugImgEntity toEntityFromDomain(DrugImage d){
		return ApiDataDrugImgEntity.builder()
			.drugId(d.getDrugId())
			.pillImage(d.getImageUrl())
			.build();
	}

	public static List<ApiDataDrugImgEntity> toEntityListFromDomainList(List<DrugImage> drugImageList) {
		return drugImageList.stream().map(DrugImageMapper::toEntityFromDomain).toList();
	}

	public static List<DrugImage> toDomainListFromEntityList(List<ApiDataDrugImgEntity> drugImageEntityList) {
		return drugImageEntityList.stream().map(DrugImageMapper::toDomainFromEntity).toList();
	}
}
