package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugGptEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugGptEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

@Repository
@RequiredArgsConstructor
public class GptEmbeddingLoadingAdapter implements EmbeddingLoadingPort {
    private final GovDrugGptEmbedJpaRepository govDrugGptEmbedJpaRepository;
    private final GovDrugJpaRepository govDrugJpaRepository;

    @Override
    public List<Drug> loadEmbeddingsByPage(Pageable pageable) {
        List<DrugRawDataEntity> rawDataEntities = govDrugJpaRepository.findAll(pageable).getContent();
        List<DrugGptEmbedEntity> drugGptEmbedEntities = govDrugGptEmbedJpaRepository.findAll(pageable).getContent();
        log("loadEmbeddingsByPage - " + pageable.getPageNumber() +"페이지 에서 받아온 drug 상세정보 수: " + rawDataEntities.size());
        log("loadEmbeddingsByPage - " + pageable.getPageNumber() +"페이지 에서 받아온 drug 임베딩 벡터수: " + drugGptEmbedEntities.size());

        // drugGptEmbedEntities를 Map으로 변환 (key: drugId)
        Map<Long, DrugGptEmbedEntity> gptEmbedMap = new HashMap<>();
        for (DrugGptEmbedEntity embed : drugGptEmbedEntities) {
            log("loadEmbeddingsByPage - " +pageable.getPageNumber()+"페이지에서 임베딩 벡터 받아온 약품 ID: " + embed.getDrugId());
            gptEmbedMap.put(embed.getDrugId(), embed);
        }

        List<Drug> drugs = new ArrayList<>();
        log("loadEmbeddingsByPage - Drug 도메인 객체 생성 시작");
        for (DrugRawDataEntity drugRawData : rawDataEntities) {
            DrugGptEmbedEntity embed = gptEmbedMap.get(drugRawData.getDrugId());
            if(embed == null) {
                log("loadEmbeddingsByPage - " + "Drug 도메인 객체 생성 대상 " + drugRawData.getDrugId() + "의 벡터가 없으므로 skip");
                continue;
            }
            log("loadEmbeddingsByPage - Drug 도메인 객체 생성 대상 " + drugRawData.getDrugId() + "의 벡터 길이: " + embed.getGptVector().length());
            Drug drug = toDomainFromEntity(drugRawData, embed);
            drugs.add(drug);
        }
        log("loadEmbeddingsByPage - Drug 도메인 객체 생성 완료");
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
