package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.batch.step.detail.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;


/**
 * ObjectMapper를 통해 JSON 응답 데이터를 매핑하기 위한 DTO 클래스입니다.
 * 의약품 상세 정보 수집 배치 작업에서 사용됩니다.
 * <p>
 * 일부 필드는 @JsonProperty를 통해 외부 필드명과 매핑되며,
 * 생성자에서는 일반의약품 여부(isGeneral)를 ETC_OTC_CODE 값으로 판별합니다.
 *
 * @field drugId       의약품 ID
 * @field drugName     의약품 이름
 * @field company      제조사/판매사 이름
 * @field permitDate   허가일
 * @field cancelDate   취소일
 * @field cancelName   취소 유형명
 * @field isGeneral    일반의약품 여부
 * @field isHerbal     한약재 여부
 * @field materialInfo 성분 정보
 * @field storeMethod  보관 방법
 * @field validTerm    유효기간
 * @field efficacy     효능
 * @field usage        사용법
 * @field precaution   주의사항
 * @since 2025-04-21
 */
@Getter
@ToString
public class DrugDetailRequest {

    @JsonProperty("ITEM_SEQ")
    private Long drugId;

    @JsonProperty("ITEM_NAME")
    private String drugName;

    @JsonProperty("ENTP_NAME")
    private String company;

    @JsonProperty("ITEM_PERMIT_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate permitDate;

    @JsonProperty("CANCEL_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate cancelDate;

    @JsonProperty("CANCEL_NAME")
    private String cancelName;

    private boolean isGeneral;

    private boolean isHerbal;

    private String materialInfo;

    @JsonProperty("STORAGE_METHOD")
    private String storeMethod;

    @JsonProperty("VALID_TERM")
    private String validTerm;

    private String efficacy;
    private String usage;
    private String precaution;

    /**
     * ETC_OTC_CODE 값을 기반으로 일반의약품 여부를 판단합니다.
     *
     * @param drugType "전문의약품"이면 false, 그 외는 true
     * @author 함예정
     * @since 2025-04-21
     */
    @JsonCreator
    public DrugDetailRequest(@JsonProperty("ETC_OTC_CODE") String drugType) {
        this.isGeneral = !"전문의약품".equals(drugType);
    }

    public void changeMaterialInfo(String materialInfo) {
        this.materialInfo = materialInfo;
    }

    public void changeUsage(String usage) {
        this.usage = usage;
    }

    public void changeEfficacy(String efficacy) {
        this.efficacy = efficacy;
    }

    public void changePrecaution(String precaution) {
        this.precaution = precaution;
    }

    public void changeIsHerbal(boolean isHerbal) {
        this.isHerbal = isHerbal;
    }
}