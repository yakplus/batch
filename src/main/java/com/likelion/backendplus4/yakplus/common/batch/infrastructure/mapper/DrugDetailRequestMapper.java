package com.likelion.backendplus4.yakplus.common.batch.infrastructure.mapper;

import com.likelion.backendplus4.yakplus.common.batch.infrastructure.detail.dto.DrugDetailRequest;
import com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity.DrugDetailEntity;
/**
 * 약품 상세 정보 요청을 엔티티로 변환하는 매퍼 클래스입니다.
 *
 * @since 2025-05-02
 */
public class DrugDetailRequestMapper {

    /**
     * DrugDetailRequest 객체를 DrugDetailEntity 객체로 변환합니다.
     *
     * @param r 변환할 DrugDetailRequest 객체
     * @return 변환된 DrugDetailEntity 객체
     * @author 함예정
     * @since 2025-05-02
     */
    public static DrugDetailEntity toEntityFromRequest(DrugDetailRequest r) {
        return DrugDetailEntity.builder()
                .drugId(r.getDrugId())
                .drugName(r.getDrugName())
                .company(r.getCompany())
                .permitDate(r.getPermitDate())
                .isGeneral(r.isGeneral())
                .materialInfo(r.getMaterialInfo())
                .storeMethod(r.getStoreMethod())
                .validTerm(r.getValidTerm())
                .efficacy(r.getEfficacy())
                .usage(r.getUsage())
                .precaution(r.getPrecaution())
                .cancelDate(r.getCancelDate())
                .cancelName(r.getCancelName())
                .isHerbal(r.isHerbal())
                .build();
    }
}
