package com.likelion.backendplus4.yakplus.drug.index.support.EmbeddingUtil;

import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.embed.infrastructure.persistence.entity.EmbeddingEntity;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.support.mapper.DrugFieldTypeMapper;

public class DrugEntityMapper {
    /**
     * DrugRawDataEntity와 EmbeddingEntity를 결합하여 Drug 도메인 객체로 변환합니다.
     *
     * @param <E>         EmbeddingEntity를 상속하는 임베딩 엔티티 타입
     * @param drugEntity  DB에서 조회한 원시 약품 데이터 엔티티
     * @param embedEntity 해당 약품의 임베딩 정보를 담고 있는 엔티티
     * @return 원시 데이터와 임베딩 정보를 포함한 Drug 도메인 객체
     * @author 이해창
     * @since 2025-05-03
     */
    public static <E extends EmbeddingEntity> Drug toDomainFromEntity(DrugRawDataEntity drugEntity, E embedEntity) {
        return Drug.builder()
                .drugId(drugEntity.getDrugId())
                .drugName(drugEntity.getDrugName())
                .company(drugEntity.getCompany())
                .permitDate(drugEntity.getPermitDate())
                .isGeneral(drugEntity.isGeneral())
                .materialInfo(DrugFieldTypeMapper.parseMaterials(drugEntity.getMaterialInfo()))
                .storeMethod(drugEntity.getStoreMethod())
                .validTerm(drugEntity.getValidTerm())
                .efficacy(DrugFieldTypeMapper.parseStringToList(drugEntity.getEfficacy()))
                .usage(DrugFieldTypeMapper.parseStringToList(drugEntity.getUsage()))
                .precaution(DrugFieldTypeMapper.parsePrecaution(drugEntity.getPrecaution()))
                .imageUrl(drugEntity.getImageUrl())
                .cancelDate(drugEntity.getCancelDate())
                .cancelName(drugEntity.getCancelName())
                .isHerbal(drugEntity.getIsHerbal())
                .vector(DrugFieldTypeMapper.parseJsonToFloatArray(embedEntity.getVector()))
                .build();
    }
}
