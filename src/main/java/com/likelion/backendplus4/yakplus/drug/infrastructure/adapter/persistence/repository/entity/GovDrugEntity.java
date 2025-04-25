package com.likelion.backendplus4.yakplus.drug.infrastructure.adapter.persistence.repository.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="GOV_DRUG_RAW_DATA")
public class GovDrugEntity {
	@Id
	@Column(name="ITEM_SEQ")
	private Long drugId;

	@Column( name= "ITEM_NAME", columnDefinition = "TEXT")
	private String drugName;

	@Column( name= "ENTP_NAME")
	private String company;

	@Column( name= "ITEM_PERMIT_DATE")
	private LocalDate permitDate;

	@Column(name = "ETC_OTC_CODE")
	private boolean isGeneral;

	@Column(name = "MATERIAL_NAME", columnDefinition = "TEXT")
	private String materialInfo;

	@JsonProperty("STORAGE_METHOD")
	@Column(name = "STORAGE_METHOD", columnDefinition = "TEXT")
	private String storeMethod;

	@Column(name = "VALID_TERM")
	private String validTerm;

	@Column(name = "EE_DOC_DATA",  columnDefinition = "TEXT")
	private String efficacy;

	@Column(name = "UD_DOC_DATA", columnDefinition = "TEXT")
	private String usage;

	@Column(name = "NB_DOC_DATA", columnDefinition = "TEXT")
	private String precaution;

	@Column(name= "IMG_URL")
	private String imageUrl;

	@Column(name= "GPT_VECTOR", columnDefinition = "JSON")
	private String gptVector;

	@Column(name= "KR_SBERT_VECTOR",columnDefinition = "JSON")
	private String sbertVector;

	@Column(name= "KM_BERT_VECTOR",columnDefinition = "JSON")
	private String kmBertVector;
}
