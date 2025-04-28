package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugGptEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugGptEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Qualifier("gptAdapter")
public class GptEmbeddingLoadingAdapter implements EmbeddingLoadingPort {
    private final GovDrugGptEmbedJpaRepository govDrugGptEmbedJpaRepository;
    private final GovDrugJpaRepository govDrugJpaRepository;


    @Override
    public List<Drug> loadAllEmbeddings() {
        List<DrugRawDataEntity> rawDataEntities = govDrugJpaRepository.findAll();
        List<DrugGptEmbedEntity> drugGptEmbedEntities = govDrugGptEmbedJpaRepository.findAll();

        // drugGptEmbedEntities를 Map으로 변환 (key: drugId)
        Map<Long, DrugGptEmbedEntity> gptEmbedMap = new HashMap<>();
        for (DrugGptEmbedEntity embed : drugGptEmbedEntities) {
            gptEmbedMap.put(embed.getDrugId(), embed);
        }

        List<Drug> drugs = new ArrayList<>();
        for (DrugRawDataEntity drugRawData : rawDataEntities) {
            DrugGptEmbedEntity embed = gptEmbedMap.get(drugRawData.getDrugId());
            Drug drug = toDomainFromEntity(drugRawData, embed);
            drugs.add(drug);
        }

        return drugs;
    }

    private static Drug toDomainFromEntity(DrugRawDataEntity drugEntity, DrugGptEmbedEntity embedEntity) {
        return Drug.builder()
                .drugId(drugEntity.getDrugId())
                .drugName(drugEntity.getDrugName())
                .company(drugEntity.getCompany())
                .permitDate(drugEntity.getPermitDate())
                .isGeneral(drugEntity.isGeneral())
                .materialInfo(DrugMapper.parseMaterials(drugEntity.getMaterialInfo()))
                .storeMethod(drugEntity.getStoreMethod())
                .validTerm(drugEntity.getValidTerm())
                .efficacy(DrugMapper.parseStringToList(drugEntity.getEfficacy()))
                .usage(DrugMapper.parseStringToList(drugEntity.getUsage()))
                .precaution(DrugMapper.parsePrecaution(drugEntity.getPrecaution()))
                .imageUrl(drugEntity.getImageUrl())
                .vector(DrugMapper.parseJsonToFloatArray(embedEntity.getGptVector()))
                .build();
    }



}
