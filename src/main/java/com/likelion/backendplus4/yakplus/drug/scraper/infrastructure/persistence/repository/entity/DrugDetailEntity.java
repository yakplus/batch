package com.likelion.backendplus4.yakplus.drug.scraper.infrastructure.persistence.repository.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "gov_drug_detail")
public class DrugDetailEntity {

	@Id
	@Column( name= "ITEM_SEQ")
	private Long drugId;

	@Column( name= "ITEM_NAME", columnDefinition = "TEXT")
	private String drugName;

	@Column( name= "ENTP_NAME")
	private String company;

	@Column( name= "ITEM_PERMIT_DATE")
	private LocalDate permitDate;

	@Column(name = "ETC_OTC_CODE")
	private boolean isGeneral;

	@Column(name = "MATERIAL_NAME", columnDefinition = "JSON")
	private String materialInfo;

	@Column(name = "STORAGE_METHOD", columnDefinition = "TEXT")
	private String storeMethod;

	@Column(name = "VALID_TERM")
	private String validTerm;

	@Column(name = "EE_DOC_DATA", columnDefinition = "JSON")
	private String efficacy;

	@Column(name = "UD_DOC_DATA", columnDefinition = "JSON")
	private String usage;

	@Column(name = "NB_DOC_DATA", columnDefinition = "JSON")
	private String precaution;

	@Column(name="CANCEL_DATE")
	private LocalDate cancelDate;

	@Column(name="CANCEL_NAME")
	private String cancelName;

	@Column(name="IS_HERBAL")
	@Builder.Default
	private Boolean isHerbal = false;

}
