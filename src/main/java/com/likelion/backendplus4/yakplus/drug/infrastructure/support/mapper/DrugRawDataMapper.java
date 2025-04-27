package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugRawDataEntity;

public class DrugRawDataMapper {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static DrugRawDataEntity toEntityFromDomain(DrugRawData raw) {
		return DrugRawDataEntity
			.builder()
			.drugId(raw.getDrugId())
			.drugName(raw.getDrugName())
			.company(raw.getCompany())
			.permitDate(raw.getPermitDate())
			.isGeneral(raw.isGeneral())
			.materialInfo(toStringFromObj(raw.getMaterialInfo()))
			.storeMethod(raw.getStoreMethod())
			.validTerm(raw.getValidTerm())
			.efficacy(toStringFromObj(raw.getEfficacy()))
			.usage(toStringFromObj(raw.getUsage()))
			.precaution(toStringFromObj(raw.getPrecaution()))
			.imageUrl(raw.getImageUrl())
			.build();
	}

	private static String toStringFromObj(Object obj){
		try{
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("변환 실패");
			return null;
		}
	}
}
