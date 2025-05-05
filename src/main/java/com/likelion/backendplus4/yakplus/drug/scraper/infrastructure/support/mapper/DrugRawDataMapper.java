package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.support.parser.JsonArrayTextParser;

public class DrugRawDataMapper {
	public static Drug toDomainFromEntity(DrugRawDataEntity e) {
		return Drug.builder()
			.drugId(e.getDrugId())
			.drugName(e.getDrugName())
			.company(e.getCompany())
			.permitDate(e.getPermitDate())
			.isGeneral(e.isGeneral())
			.materialInfo(DrugFieldTypeMapper.parseMaterials(e.getMaterialInfo()))
			.storeMethod(e.getStoreMethod())
			.validTerm(e.getValidTerm())
			.efficacy(JsonArrayTextParser.extractAndClean(e.getEfficacy()))
			.usage(DrugFieldTypeMapper.parseStringToList(e.getUsage()))
			.precaution(DrugFieldTypeMapper.parsePrecaution(e.getPrecaution()))
			.imageUrl(e.getImageUrl())
			.cancelDate(e.getCancelDate())
			.cancelName(e.getCancelName())
			.isHerbal(e.getIsHerbal())
			.build();
	}
}
