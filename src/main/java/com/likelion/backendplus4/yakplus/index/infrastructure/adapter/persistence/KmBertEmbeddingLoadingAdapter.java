package com.likelion.backendplus4.yakplus.index.infrastructure.adapter.persistence;

import com.likelion.backendplus4.yakplus.common.util.log.LogLevel;
import com.likelion.backendplus4.yakplus.drug.domain.model.Drug;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugKmBertEmbedEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugRawDataEntity;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugJpaRepository;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.jpa.GovDrugKmBertEmbedJpaRepository;
import com.likelion.backendplus4.yakplus.index.application.port.out.EmbeddingLoadingPort;
import com.likelion.backendplus4.yakplus.index.exception.IndexException;
import com.likelion.backendplus4.yakplus.index.exception.error.IndexErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.likelion.backendplus4.yakplus.common.util.log.LogUtil.log;

@Repository
@RequiredArgsConstructor
public class KmBertEmbeddingLoadingAdapter implements EmbeddingLoadingPort {
    private final GovDrugKmBertEmbedJpaRepository govDrugKmBertEmbedJpaRepository;

    @Override
    public List<Drug> loadEmbeddingsByPage(Pageable pageable) {
        List<Drug> drugs = new ArrayList<>();
        List<Object[]> rows = govDrugKmBertEmbedJpaRepository.findRawAndEmbed(pageable);
        log("loadEmbeddingsByPage - " + pageable.getPageNumber() +"페이지 에서 받아온 drug 객체 제작 대상 데이터 수: " + rows.size());
        if (rows.isEmpty()) {
            log(LogLevel.ERROR,"loadEmbeddingsByPage - Drug 도메인 객체 생성 대상 데이터 없음");
            throw new IndexException(IndexErrorCode.RAW_DATA_FETCH_ERROR);
        }
        for (Object[] arr : rows) {
            DrugRawDataEntity   raw   = (DrugRawDataEntity)   arr[0];
            DrugKmBertEmbedEntity embed = (DrugKmBertEmbedEntity)  arr[1];
            drugs.add(toDomainFromEntity(raw, embed));
        }
        log("loadEmbeddingsByPage - Drug 도메인 객체 생성 완료");

        return drugs;
    }

    private static Drug toDomainFromEntity(DrugRawDataEntity drugEntity, DrugKmBertEmbedEntity embedEntity) {
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
                .cancelDate(drugEntity.getCancelDate())
                .cancelName(drugEntity.getCancelName())
                .isHerbal(drugEntity.getIsHerbal())
                .vector(DrugMapper.parseJsonToFloatArray(embedEntity.getKmBertVector()))
                .build();
    }

}
