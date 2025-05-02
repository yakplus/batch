package com.likelion.backendplus4.yakplus.drug.infrastructure.persistence.repository.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TableCombineEntity {
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
