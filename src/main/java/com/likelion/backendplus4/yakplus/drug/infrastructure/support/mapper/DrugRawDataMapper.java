package com.likelion.backendplus4.yakplus.drug.infrastructure.support.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.domain.exception.error.ScraperErrorCode;
import com.likelion.backendplus4.yakplus.drug.domain.model.DrugRawData;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.index.support.parser.JsonArrayTextParser;
import java.io.IOException;
import java.util.List;

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


	public static Drug toDomainFromEntity(DrugRawDataEntity e) {
		List<String> efficacy;
		try {
			efficacy = JsonArrayTextParser.extractAndClean(e.getEfficacy());
		} catch (IOException exception) {
			throw new ScraperException(ScraperErrorCode.PARSING_ERROR);
		}

		return Drug.builder()
				.drugId(e.getDrugId())
				.drugName(e.getDrugName())
				.company(e.getCompany())
				.permitDate(e.getPermitDate())
				.isGeneral(e.isGeneral())
// TODO			.materialInfo(JsonArrayTextParser.extractAndClean(e.getMaterialInfo()))
				.storeMethod(e.getStoreMethod())
				.validTerm(e.getValidTerm())
				.efficacy(efficacy)
// TODO			.usage(e.getUsage())
// TODO			.precaution(e.getPrecaution())
				.imageUrl(e.getImageUrl())
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
