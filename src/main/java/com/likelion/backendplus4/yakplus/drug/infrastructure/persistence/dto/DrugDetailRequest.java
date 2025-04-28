package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

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


	private boolean isGeneral;
	private String materialInfo;

	@JsonProperty("STORAGE_METHOD")
	private String storeMethod;

	@JsonProperty("VALID_TERM")
	private String validTerm;

	private String efficacy;
	private String usage;
	private String precaution;

	@JsonCreator
	public DrugDetailRequest(@JsonProperty("ETC_OTC_CODE") String drugType) {
		this.isGeneral = !"전문의약품".equals(drugType);
	}

	public void changeMaterialInfo(String materialInfo){
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
}