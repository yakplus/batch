package com.likelion.backendplus4.yakplus.temp.support.mapper;

import com.likelion.backendplus4.yakplus.drug.domain.exception.ScraperException;
import com.likelion.backendplus4.yakplus.drug.domain.exception.error.ScraperErrorCode;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence.DrugMapper;
import com.likelion.backendplus4.yakplus.index.support.parser.JsonArrayTextParser;
import java.io.IOException;
import java.util.List;

public class TempDrugRawDataMapper {

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
    			.materialInfo(DrugMapper.parseMaterials(e.getMaterialInfo()))
                .storeMethod(e.getStoreMethod())
                .validTerm(e.getValidTerm())
                .efficacy(efficacy)
    			.usage(DrugMapper.parseStringToList(e.getUsage()))
    			.precaution(DrugMapper.parsePrecaution(e.getPrecaution()))
                .imageUrl(e.getImageUrl())
                .cancelDate(e.getCancelDate())
                .cancelName(e.getCancelName())
                .isHerbal(e.isHerbal())
                .build();
    }

}
