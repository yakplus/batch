package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.combine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 의약품 상세 정보와 이미지 데이터를 병합한 DTO 클래스입니다.
 * <p>
 * DrugDetailEntity와 ApiDataDrugImgEntity를 조인하여 구성되며,
 * 병합 테이블 생성 또는 저장을 위한 중간 구조로 사용됩니다.
 *
 * @field drugId       의약품 ID
 * @field drugName     의약품 이름
 * @field company      제조사
 * @field permitDate   허가일
 * @field isGeneral    일반의약품 여부
 * @field materialInfo 성분 정보
 * @field storeMethod  보관 방법
 * @field validTerm    유효기간
 * @field efficacy     효능
 * @field usage        사용법
 * @field precaution   주의사항
 * @field cancelDate   취소일
 * @field cancelName   취소 유형명
 * @field isHerbal     한약 여부
 * @field productImage 제품 이미지 URL
 * @field pillImage    낱알 이미지 URL
 * @since 2025-05-02
 */
@Getter
@AllArgsConstructor
public class TableCombineDto {
    private Long drugId;
    private String drugName;
    private String company;
    private LocalDate permitDate;
    private boolean isGeneral;
    private String materialInfo;
    private String storeMethod;
    private String validTerm;
    private String efficacy;
    private String usage;
    private String precaution;
    private LocalDate cancelDate;
    private String cancelName;
    private Boolean isHerbal;
    private String productImage;
    private String pillImage;
}
