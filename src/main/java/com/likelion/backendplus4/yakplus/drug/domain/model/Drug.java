package com.likelion.backendplus4.yakplus.drug.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 의약품 정보를 담는 도메인 객체입니다.
 */
@Builder
@Getter
@ToString
public class Drug {
	private Long drugId;
	private String drugName;
	private String company;
	private LocalDate permitDate;
	private boolean isGeneral;
	private List<Material> materialInfo;
	private String storeMethod;
	private String validTerm;
	private List<String> efficacy;
	private List<String> usage;
	private Map<String, List<String>> precaution;
	private String imageUrl;
	private float[] vector;
	private LocalDate cancelDate;
	private String cancelName;
	private boolean isHerbal;
}
