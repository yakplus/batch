package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.application.service.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.application.service.exception.error.ScraperErrorCode;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.DrugMapper;
import com.likelion.backendplus4.yakplus.index.support.parser.JsonArrayTextParser;
import java.io.IOException;
import java.util.List;

public class DrugRawDataMapper {
	private static final ObjectMapper objectMapper = new ObjectMapper();


	public static Drug toDomainFromEntity(DrugRawDataEntity e, List<String> parsedEfficacy) {
		return Drug.builder()
				.drugId(e.getDrugId())
				.drugName(e.getDrugName())
				.company(e.getCompany())
				.permitDate(e.getPermitDate())
				.isGeneral(e.isGeneral())
				.materialInfo(DrugMapper.parseMaterials(e.getMaterialInfo()))
				.storeMethod(e.getStoreMethod())
				.validTerm(e.getValidTerm())
				.efficacy(parsedEfficacy)
				.usage(DrugMapper.parseStringToList(e.getUsage()))
				.precaution(DrugMapper.parsePrecaution(e.getPrecaution()))
				.imageUrl(e.getImageUrl())
				.cancelDate(e.getCancelDate())
				.cancelName(e.getCancelName())
				.isHerbal(e.getIsHerbal())
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
