package com.likelion.backendplus4.yakplus.drug.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 외부 API로부터 수집된 의약품 원시 데이터를 담는 객체입니다.
 */
@Builder
@Getter
@ToString
public class DrugRawData {
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
	private LocalDate cancelDate;
	private String cancelName;
	private boolean isHerbal;
}
