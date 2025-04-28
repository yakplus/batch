package com.likelion.backendplus4.yakplus.index.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.likelion.backendplus4.yakplus.drug.domain.model.vo.Material;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drug {
	private Long drugId;
	private String drugName;
	private String company;
	private List<String> efficacy;
	private float[] vector;
	private LocalDate permitDate;
	private boolean isGeneral;
	private List<Material> materialInfo;
	private String storeMethod;
	private String validTerm;
	private List<String> usage;
	private Map<String, List<String>> precaution;
	private String imageUrl;
}