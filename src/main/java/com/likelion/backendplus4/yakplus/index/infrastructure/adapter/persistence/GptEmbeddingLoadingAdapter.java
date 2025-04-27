package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugGptEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugGptEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.jpa.GovDrugJpaRepository;
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
                .materialInfo(parseMaterials(drugEntity.getMaterialInfo()))
                .storeMethod(drugEntity.getStoreMethod())
                .validTerm(drugEntity.getValidTerm())
                .efficacy(parseStringToList(drugEntity.getEfficacy()))
                .usage(parseStringToList(drugEntity.getUsage()))
                .precaution(parsePrecaution(drugEntity.getPrecaution()))
                .imageUrl(drugEntity.getImageUrl())
                .vector(parseJsonToFloatArray(embedEntity.getGptVector()))
                .build();
    }

    public static List<Material> parseMaterials(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<Material>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Material 파싱 실패", e);
        }
    }

    public static List<String> parseStringToList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("String to list 파싱 실패", e);
        }
    }

    public static Map<String, List<String>> parsePrecaution(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("precaution 파싱 실패", e);
        }
    }

    public static float[] parseJsonToFloatArray(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, float[].class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse vector JSON", e);
        }
    }

}
