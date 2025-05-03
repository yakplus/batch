package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.mapper;

import com.likelion.backendplus4.yakplus.drug.infrastructure.batch.detail.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;

public class DrugDetailRequestMapper {

	public static DrugDetailEntity toEntityFromRequest(DrugDetailRequest r){
		return DrugDetailEntity.builder()
			.drugId(r.getDrugId())
			.drugName(r.getDrugName())
			.company(r.getCompany())
			.permitDate(r.getPermitDate())
			.isGeneral(r.isGeneral())
			.materialInfo(r.getMaterialInfo())
			.storeMethod(r.getStoreMethod())
			.validTerm(r.getValidTerm())
			.efficacy(r.getEfficacy())
			.usage(r.getUsage())
			.precaution(r.getPrecaution())
			.cancelDate(r.getCancelDate())
			.cancelName(r.getCancelName())
			.isHerbal(r.isHerbal())
			.build();
	}
}
