package com.likelion.backendplus4.yakplus.drug.infrastructure.batch.combine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrugMergeDto {
	private Long itemSeq;
	private Boolean etcOtcCode;
	private String itemPermitDate;
	private String eeDocData;
	private String entpName;
	private String imgUrl;
	private String materialName;
	private String itemName;
	private String nbDocData;
	private String storageMethod;
	private String udDocData;
	private String validTerm;
	private String cancelDate;
	private String cancelName;
	private Boolean isHerbal;
}
